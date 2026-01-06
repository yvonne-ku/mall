package com.noinch.mall.biz.product.infrastructure.es;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.noinch.mall.biz.product.domain.mode.ProductIndex;
import com.noinch.mall.biz.product.domain.repository.EsProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import lombok.AllArgsConstructor;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class EsProductRepositoryImpl implements EsProductRepository {

    /**
     * Elasticsearch 原生客户端用于更搞笑的批处理
     */
    private final ElasticsearchClient esClient;

    /**
     * Spring Data Elasticsearch 操作模板，用于执行查询等操作
     */
    private final ElasticsearchOperations esClientTemplate;

    @Override
    public List<ProductIndex> searchProduct(String description, int pageNo, int pageSize, Integer sort, Integer priceGt, Integer priceLte) {
        log.info("ES搜索请求 - 关键词: [{}], 页码: {}, 每页数量: {}, 排序字段: {}, 价格大于: {}, 价格小于等于: {}",
                description, pageNo, pageSize, sort, priceGt, priceLte);
        try {
            // 1. 构建布尔查询
            NativeQueryBuilder queryBuilder = NativeQuery.builder();
            queryBuilder.withQuery(q -> q.bool(b -> {
                // 1.1 组合搜索词（参与评分）
                b.must(m -> m.multiMatch(mm -> mm
                        .fields("name^3", "brandName^2", "categoryName^1")
                        .query(description)
                ));
                // 1.2 价格过滤
                if (priceGt != null || priceLte != null) {
                    b.filter(f -> f.range(r -> {
                        r.field("price");
                        if (priceGt != null) r.gt(JsonData.of(priceGt));
                        if (priceLte != null) r.lte(JsonData.of(priceLte));
                        return r;
                    }));
                }
                return b;
            }));

            // 2. 分页设置
            queryBuilder.withPageable(PageRequest.of(Math.max(pageNo, 1) - 1, pageSize));

            // 3. 排序设置 (0: 默认评分, 1: 价格升序, 2: 价格降序)
            if (sort != null) {
                if (sort == 1) {
                    queryBuilder.withSort(s -> s.field(f -> f.field("price").order(SortOrder.Asc)));
                } else if (sort == 2) {
                    queryBuilder.withSort(s -> s.field(f -> f.field("price").order(SortOrder.Desc)));
                }
            }

            // 4. 执行查询
            NativeQuery query = queryBuilder.build();
            SearchHits<ProductIndex> hits = esClientTemplate.search(query, ProductIndex.class);

            // 5. 结果处理
            List<ProductIndex> result = hits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());

            log.info("ES搜索成功 - 匹配总数: {}, 当前返回数量: {}", hits.getTotalHits(), result.size());
            return result;

        } catch (Exception e) {
            log.error("ES搜索异常 - 关键词: [{}], 错误信息: {}", description, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * 批量保存或全量更新
     */
    @Override
    public void saveBatch(List<ProductIndex> productIndexList) {
        if (CollectionUtils.isEmpty(productIndexList)) return;
        try {
            // 1. 构建 BulkOperation 列表
            List<BulkOperation> operations = productIndexList.stream()
                    .filter(Objects::nonNull)   // 上游来的 list 可能混入 null 元素
                    .map(item -> BulkOperation.of(b -> b
                            .index(i -> i
                                    .index("product_index_test") // 索引名
                                    .id(item.getId())      // 文档ID
                                    .document(item)        // 文档内容
                            )
                    ))
                    .collect(Collectors.toList());

            // 2. 创建并执行 BulkRequest
            BulkRequest bulkRequest = BulkRequest.of(r -> r.operations(operations));
            var response = esClient.bulk(bulkRequest);

            // 3. 结果分析 (BulkResponse)
            if (response.errors()) {
                // errors() 为 true 表示这批数据里至少有一条失败了
                response.items().forEach(item -> {
                    if (item.error() != null) {
                        log.error("文档 ID: {} 处理失败, 原因: {}", item.id(), item.error().reason());
                    }
                });
            } else {
                log.info("ES 批量保存成功, 耗时: {}ms", response.took());
            }
        } catch (IOException e) {
            log.error("ES 批量操作发生 IO 异常", e);
        }
    }

    /**
     * 保存或全量更新
     * 在 ES 中，save 方法具有 "Upsert" 语义：ID 不存在则新增，存在则全量替换
     */
    @Override
    public void save(ProductIndex productIndex) {
        try {
            esClientTemplate.save(productIndex);
        } catch (Exception e) {
            log.error("ES 保存文档失败, id: {}", productIndex.getId(), e);
        }
    }

    /**
     * 删除所有商品
     */
    @Override
    public void deleteAll() {
        try {
            // 构建一个匹配所有文档的查询
            Query query = NativeQuery.builder()
                    .withQuery(q -> q.matchAll(m -> m))
                    .build();

            // 执行删除
            esClientTemplate.delete(query, ProductIndex.class);
            log.info("ES 索引数据已成功清空");
        } catch (Exception e) {
            log.error("清空 ES 索引数据失败", e);
            throw new RuntimeException("ES 清空失败", e);
        }
    }

    /**
     * 根据 ID 删除
     */
    @Override
    public void deleteById(String id) {
        try {
            esClientTemplate.delete(id, ProductIndex.class);
        } catch (Exception e) {
            log.error("ES 删除文档失败, id: {}", id, e);
        }
    }
}
