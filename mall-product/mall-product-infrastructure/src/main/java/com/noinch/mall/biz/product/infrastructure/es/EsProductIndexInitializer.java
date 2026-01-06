package com.noinch.mall.biz.product.infrastructure.es;

import com.noinch.mall.biz.product.domain.mode.ProductIndex;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EsProductIndexInitializer {

    private final ElasticsearchOperations elasticsearchOperations;

    @PostConstruct
    public void initIndex() {
        IndexOperations indexOps = elasticsearchOperations.indexOps(ProductIndex.class);

        if (!indexOps.exists()) {
            indexOps.create();
            indexOps.putMapping();  // 写入映射 (创建 mappings)，它会读取 @Field 里的 analyzer 配置
            System.out.println("ES 索引 [product_index_test] 创建成功！");
        }
    }
}
