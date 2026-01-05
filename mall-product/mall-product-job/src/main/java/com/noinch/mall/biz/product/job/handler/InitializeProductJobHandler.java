
package com.noinch.mall.biz.product.job.handler;

import com.alibaba.fastjson2.JSON;
import com.noinch.mall.biz.product.domain.mode.ProductIndex;
import com.noinch.mall.biz.product.domain.repository.EsProductRepository;
import com.noinch.mall.biz.product.infrastructure.dao.entity.ProductBrandDO;
import com.noinch.mall.biz.product.infrastructure.dao.entity.ProductCategoryDO;
import com.noinch.mall.biz.product.infrastructure.dao.mapper.ProductBrandMapper;
import com.noinch.mall.biz.product.infrastructure.dao.mapper.ProductCategoryMapper;
import com.noinch.mall.biz.product.infrastructure.dao.mapper.ProductSpuMapper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.product.infrastructure.dao.entity.ProductSpuDO;
import com.noinch.mall.biz.product.infrastructure.dao.entity.ProductSkuDO;
import com.noinch.mall.biz.product.infrastructure.dao.mapper.ProductSkuMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * 初始化商品任务，通过并发编程完成生产-消费模型，达到快速同步的效果
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitializeProductJobHandler extends IJobHandler {
    
    /**
     * 商品 SKU 持久层
     */
    private final ProductSkuMapper productSkuMapper;

    /**
     * 商品 SPU 持久层
     */
    private final ProductSpuMapper productSpuMapper;

     /**
     * 商品品牌持久层
     */
    private final ProductBrandMapper productBrandMapper;

     /**
     * 商品分类持久层
     */
    private final ProductCategoryMapper productCategoryMapper;

    /**
     * 商品索引 ElasticSearch 持久层
     */
    private final EsProductRepository esProductRepository;

    /**
     * Hippo4j 线程池，执行同步程序
     */
    private final ThreadPoolExecutor productSkuInitSyncThreadPoolExecutor;
    
    /**
     * 单次同步 ElasticSearch 数量
     */
    private static final Integer MAX_SYNC_SIZE = 5000;
    
    /**
     * 阻塞队列最大容量，相当于一个缓冲池大小
     */
    private static final Integer MAX_POOL_SIZE = 200000;
    
    /**
     * 记录开始时间
     */
    private static Long START_TIME = 0L;
    
    /**
     * 记录同步
     */
    private static final AtomicInteger COUNT_NUM = new AtomicInteger(0);
    
    /**
     * 记录实际同步数量
     */
    private static final LongAdder SYNC_SUM = new LongAdder();
    
    /**
     * 打印输出监控定时器
     */
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    /**
     * 商品 SKU 初始化同步任务
     */
    @XxlJob(value = "productSkuSyncInitJobHandler")
    @Override
    public void execute() throws Exception {
        // 定时打印执行进度
        printPoolAndScheduledInfo();
        // 初始化的全量同步，先清空旧数据
        esProductRepository.deleteAll();
        // 执行商品 SKU 同步程序
        executeProductSkuSync();
        // 释放定时器、同步线程池资源
        shutdownPoolAndPrintCountSize();
    }

    void executeProductSkuSync() {
        BlockingQueue<ProductSkuDO> blockingQueueCachePool = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
        productSkuMapper.listAllProductSkuStreamQuery(resultContext -> {
            // 记录流式查询总数量
            COUNT_NUM.incrementAndGet();
            // 每次向缓冲池添加 MAX_SYNC_SIZE 记录
            try {
                blockingQueueCachePool.put(resultContext.getResultObject());
            } catch (Exception ex) {
                log.error("商品SKU基础数据初始化流程, 添加阻塞队列缓冲池失败, 数据记录: {}",
                        JSON.toJSONString(resultContext.getResultObject()), ex);
            }
            // 避免请求目标数据库（ElasticSearch 或其它）次数过多，所以建议每次 MAX_SYNC_SIZE 条数，虽然可能不够这个数
            if (blockingQueueCachePool.size() >= MAX_SYNC_SIZE) {
                productSkuInitSyncThreadPoolExecutor.execute(() -> executeTask(blockingQueueCachePool));
            }
        });
        // 兜底，将最后缓冲的任务执行
        productSkuInitSyncThreadPoolExecutor.execute(() -> executeTask(blockingQueueCachePool));
    }

    /**
     * 批量一次性处理好 ProductSpuDO、ProductBrandDO、ProductCategoryDO 相关数据
     */
    private Object[] getMapsForBatch(List<ProductSkuDO> productSkuDOList) {
        // 批量一次性处理好 ProductSpuDO 相关数据
        Set<Long> spuIds = productSkuDOList.stream().map(ProductSkuDO::getProductId).collect(Collectors.toSet());
        List<ProductSpuDO> spuList = productSpuMapper.selectBatchIds(spuIds);
        Map<Long, ProductSpuDO> spuMap = spuList.stream().collect(Collectors.toMap(ProductSpuDO::getId, s -> s));

        // 批量一次性处理好 ProductBrandDO 相关数据
        Set<Long> brandIds = productSkuDOList.stream().map(ProductSkuDO::getBrandId).collect(Collectors.toSet());
        List<ProductBrandDO> brandList = productBrandMapper.selectBatchIds(brandIds);
        Map<Long, ProductBrandDO> brandMap = brandList.stream().collect(Collectors.toMap(ProductBrandDO::getId, s -> s));

        // 批量一次性处理好 ProductCategoryDO 相关数据
        Set<Long> categoryIds = productSkuDOList.stream().map(ProductSkuDO::getCategoryId).collect(Collectors.toSet());
        List<ProductCategoryDO> categoryList = productCategoryMapper.selectBatchIds(categoryIds);
        Map<Long, ProductCategoryDO> categoryMap = categoryList.stream().collect(Collectors.toMap(ProductCategoryDO::getId, s -> s));

        return new Object[] { spuMap, brandMap, categoryMap };
    }

    /**
     * 执行同步任务
     */
    private void executeTask(BlockingQueue<ProductSkuDO> blockingQueueCachePool) {
        List<ProductSkuDO> copyList = new ArrayList<>(MAX_SYNC_SIZE);

        try {
            // 统一使用 drainTo，无论是否是最后一次 drain，它都能安全地取出 0 到 MAX_SYNC_SIZE 条数据
            int len = blockingQueueCachePool.drainTo(copyList, MAX_SYNC_SIZE);
            if (len > 0) {
                Object[] mapsForBatch = getMapsForBatch(copyList);
                Map<Long, ProductSpuDO> spuMap = (Map<Long, ProductSpuDO>) mapsForBatch[0];
                Map<Long, ProductBrandDO> brandMap = (Map<Long, ProductBrandDO>) mapsForBatch[1];
                Map<Long, ProductCategoryDO> categoryMap = (Map<Long, ProductCategoryDO>) mapsForBatch[2];

                // 此处决定向何处同步数据
                List<ProductIndex> productIndexList = copyList.stream().map(productSkuDO -> {
                    ProductIndex productIndex = new ProductIndex();

                    // 从预先处理好的 Map 中取出对应的 ProductSpuDO、ProductBrandDO、ProductCategoryDO
                    Long spuId = productSkuDO.getProductId();
                    Long brandId = productSkuDO.getBrandId();
                    Long categoryId = productSkuDO.getCategoryId();
                    ProductSpuDO productSpuDO = spuMap.get(spuId);
                    ProductBrandDO productBrandDO = brandMap.get(brandId);
                    ProductCategoryDO productCategoryDO = categoryMap.get(categoryId);
                    if (productSpuDO == null || brandId == null || categoryId == null) {
                        log.error("商品SKU基础数据初始化流程, 商品SPU数据或品牌数据或分类数据不存在, 商品SKU记录: {}", JSON.toJSONString(productSkuDO));
                        return null;
                    }

                    productIndex.setId(productSkuDO.getId().toString());
                    productIndex.setSpuId(spuId.toString());
                    productIndex.setSubTitle(productSpuDO.getSubTitle());
                    productIndex.setAttribute(productSkuDO.getAttribute());
                    productIndex.setName(productSpuDO.getName());
                    productIndex.setBrandName(productBrandDO.getName());
                    productIndex.setCategoryName(productCategoryDO.getName());
                    productIndex.setPrice(productSkuDO.getPrice());
                    return productIndex;
                }).toList();

                // 此处决定向何处同步数据
                esProductRepository.saveBatch(productIndexList);
                SYNC_SUM.add(len);
            }
        } catch (Exception ex) {
            log.error("商品SKU基础数据初始化流程执行失败", ex);
        }
    }
    
    private void printPoolAndScheduledInfo() {
        START_TIME = System.currentTimeMillis();
        SCHEDULED_EXECUTOR.scheduleAtFixedRate(() -> {
            log.info("商品SKU基础数据初始化流程, 当前已同步总数量: {}", COUNT_NUM.get());
            log.info("商品SKU基础数据初始化流程, 线程池状态打印, 当前活动线程数: {}, 当前排队任务数: {}, 执行完成线程数: {}, 线程池任务总数: {}",
                    productSkuInitSyncThreadPoolExecutor.getActiveCount(),
                    productSkuInitSyncThreadPoolExecutor.getQueue().size(),
                    productSkuInitSyncThreadPoolExecutor.getCompletedTaskCount(),
                    productSkuInitSyncThreadPoolExecutor.getTaskCount());
        }, 30, 10, TimeUnit.SECONDS);
    }
    
    private void shutdownPoolAndPrintCountSize() {
        // 关闭定时器线程池
        SCHEDULED_EXECUTOR.shutdown();
        // 关闭数据同步线程池
        productSkuInitSyncThreadPoolExecutor.shutdown();
        while (true) {
            if (SCHEDULED_EXECUTOR.isTerminated() && productSkuInitSyncThreadPoolExecutor.isTerminated()) {
                log.info("商品SKU基础数据初始化流程, 总条数: {}, 同步成功数: {}, 同步执行总耗时: {}",
                        COUNT_NUM.get(),
                        SYNC_SUM.longValue(),
                        System.currentTimeMillis() - START_TIME);
                break;
            }
        }
    }
}
