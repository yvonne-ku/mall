package com.noinch.mall.biz.product.infrastructure.es;

import com.noinch.mall.biz.product.domain.mode.ProductIndex;
import com.noinch.mall.biz.product.domain.repository.EsProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class EsProductRepositoryImpl implements EsProductRepository {

    private final ElasticsearchOperations esClientTemplate;

    @Override
    public List<ProductIndex> searchProduct(String description, int pageNo, int pageSize) {
        log.info("ES搜索请求 - 关键词: [{}], 页码: {}, 每页数量: {}", description, pageNo, pageSize);
        try {
            // 1. 构建查询条件
            Query query = NativeQuery.builder()
                    .withQuery(q -> q
                            .multiMatch(m -> m
                                    .fields("name^3", "brandName^2", "categoryName^1")
                                    .query(description)
                            )
                    )
                    .withPageable(PageRequest.of(pageNo, pageSize))
                    .build();

            // 2. 执行查询
            SearchHits<ProductIndex> hits = esClientTemplate.search(query, ProductIndex.class);

            // 3. 提取并记录结果日志
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

    /**
     * 根据 ID 查找
     */
    @Override
    public ProductIndex findById(String id) {
        try {
            return esClientTemplate.get(id, ProductIndex.class);
        } catch (Exception e) {
            log.error("ES 获取文档失败, id: {}", id, e);
            return null;
        }
    }
}
