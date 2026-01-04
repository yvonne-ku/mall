package com.noinch.mall.springboot.starter.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.noinch.mall.springboot.starter.elasticsearch.config.ElasticAutoConfiguration;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ElasticAutoConfiguration.class})
class ElasticTest {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private static final String INDEX_NAME = "test_product";
    private static final String DOCUMENT_ID = "1";

    @BeforeEach
    void setUp() throws IOException {
        // 在测试开始前添加一个测试文档
        Product product = new Product();
        product.setId(DOCUMENT_ID);
        product.setName("测试产品");
        product.setDescription("这是一个测试产品");
        product.setPrice(99.99);
        product.setCategory("测试分类");

        IndexRequest<Product> indexRequest = IndexRequest.of(i -> i
                .index(INDEX_NAME)
                .id(product.getId())
                .document(product)
                .refresh(Refresh.True) // 强制刷新，确保写入后立刻能搜到
        );

        IndexResponse response = elasticsearchClient.index(indexRequest);
        assertNotNull(response);
        assertEquals(DOCUMENT_ID, response.id());
    }

    @AfterEach
    void tearDown() throws IOException {
        // 在测试结束后删除测试文档
        DeleteRequest deleteRequest = DeleteRequest.of(d -> d
                .index(INDEX_NAME)
                .id(DOCUMENT_ID)
        );

        DeleteResponse response = elasticsearchClient.delete(deleteRequest);
        assertNotNull(response);
        assertEquals(DOCUMENT_ID, response.id());
    }

    @Test
    void testElasticsearchClientConnection() {
        // 测试 ElasticsearchClient 是否成功注入
        assertNotNull(elasticsearchClient);
    }

    @Test
    void testElasticsearchOperationsConnection() {
        // 测试 ElasticsearchOperations 是否成功注入
        assertNotNull(elasticsearchOperations);
    }

    @Test
    void testGetDocument() throws IOException {
        // 使用 ElasticsearchClient 获取文档
        GetRequest getRequest = GetRequest.of(g -> g
                .index(INDEX_NAME)
                .id(DOCUMENT_ID)
        );

        GetResponse<Product> response = elasticsearchClient.get(getRequest, Product.class);
        assertNotNull(response);
        assertTrue(response.found());
        assertNotNull(response.source());
        assertEquals(DOCUMENT_ID, response.source().getId());
        assertEquals("测试产品", response.source().getName());
    }

    @Test
    void testSearchByCriteria() {
        // 使用 ElasticsearchOperations 按条件搜索文档
        Criteria criteria = Criteria.where("name").is("测试产品");
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);
        assertNotNull(searchHits);
        assertEquals(1, searchHits.getTotalHits());

        Product product = searchHits.getSearchHit(0).getContent();
        assertNotNull(product);
        assertEquals(DOCUMENT_ID, product.getId());
        assertEquals("测试产品", product.getName());
    }

    @Test
    void testSearchWithElasticsearchClient() throws IOException {
        // 使用 ElasticsearchClient 搜索文档
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX_NAME)
                .query(q -> q
                        .match(m -> m
                                .field("name")
                                .query("测试产品")
                        )
                )
        );

        SearchResponse<Product> response = elasticsearchClient.search(searchRequest, Product.class);
        assertNotNull(response);
        assertEquals(1, response.hits().total().value());

        List<Hit<Product>> hits = response.hits().hits();
        assertNotNull(hits);
        assertEquals(1, hits.size());

        Product product = hits.get(0).source();
        assertNotNull(product);
        assertEquals(DOCUMENT_ID, product.getId());
        assertEquals("测试产品", product.getName());
    }

    // 测试实体类
    @Data
    @Document(indexName = INDEX_NAME)
    static class Product {
        @Id
        private String id;

        @Field(type = FieldType.Text, analyzer = "ik_max_word")
        private String name;

        @Field(type = FieldType.Text)
        private String description;

        @Field(type = FieldType.Double)
        private Double price;

        @Field(type = FieldType.Keyword)
        private String category;
    }
}
