
package com.noinch.mall.biz.product.infrastructure.algorithm;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;
import com.noinch.mall.springboot.starter.distributedid.SnowflakeIdUtil;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;

/**
 * 基因雪花算法实现，brand_id 作为 product_id 的一部分
 *
 */
public class ProductSpuSnowflakeServiceShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {
    
    @Getter
    private Properties props;
    private int shardingCount;
    private static final String SHARDING_COUNT_KEY = "sharding-count";

    /**
     * 这两个入参是 ShardingSphere 路由引擎在解析完 SQL 后，动态生成并传入你的算法的
     * @param availableTargetNames 来自配置文件中，示例：
     *                             actual-data-nodes: db0.product_spu_$->{0..7}  # 假设分了8张表
     *                             availableTargetNames：["product_spu_0", "product_spu_1", "product_spu_2", ... "product_spu_7"]
     * @param shardingValue 来自动态 SQL 解析生成，它封装了本次 SQL 中涉及的所有分片键的查询条件，包括三个字段，示例：
     *                      SQL 语句：SELECT * FROM product_spu WHERE brand_id = 1001
     *                      logicTableName（逻辑表名）：product_spu,
     *                      columnNameAndShardingValuesMap（等值查询条件）: {
     *                          "brand_id": [1001],
     *                      },
     *                      columnNameAndRangeValuesMap（范围查询条件）: {}  // 无范围查询
     * @return 本次 SQL 真正要操作的物理表名（或数据源名）集合。
     */
    @Override
    public Collection<String> doSharding(Collection availableTargetNames, ComplexKeysShardingValue shardingValue) {
        final String brandId = "brand_id";
        final String id = "id";
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Map<String, Collection<Comparable<Long>>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        if (CollUtil.isNotEmpty(columnNameAndShardingValuesMap)) {
            Collection<Comparable<Long>> brandIdCollection = columnNameAndShardingValuesMap.get(brandId);
            Collection<Comparable<Long>> productIdCollection = columnNameAndShardingValuesMap.get(id);
            // 如果等值查询条件中有 brandId
            if (CollUtil.isNotEmpty(brandIdCollection)) {
                Comparable<?> brandIdQuery = brandIdCollection.stream().findFirst().get();
                String tableNameSuffix = String.valueOf(hashShardingValue(brandIdQuery) % shardingCount);
                result.add(shardingValue.getLogicTableName() + "_" + tableNameSuffix);
                return result;
            }
            // 如果等值查询条件中有 id
            else if (CollUtil.isNotEmpty(productIdCollection)){
                Comparable<Long> comparable = productIdCollection.stream().findFirst().get();
                // 基因雪花算法在定义的时候就已经把分片信息融合到雪花 ID 中 Gene 部分了，要查看去 starter 的定义部分查看算法逻辑
                int tableNameSuffix = SnowflakeIdUtil.parseSnowflakeServiceId(String.valueOf(comparable)).getGene();
                result.add(shardingValue.getLogicTableName() + "_" + tableNameSuffix);
                return result;
            }
        }
        return availableTargetNames;
    }
    
    @Override
    public void init(Properties props) {
        this.props = props;
        shardingCount = getShardingCount(props);
    }

    @Override
    public String getType() {
        return "CLASS_BASED";
    }

    private int getShardingCount(final Properties props) {
        Preconditions.checkArgument(props.containsKey(SHARDING_COUNT_KEY), "Sharding count cannot be null.");
        return Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY));
    }
    
    private long hashShardingValue(final Comparable<?> shardingValue) {
        return Math.abs((long) shardingValue.hashCode());
    }
}

