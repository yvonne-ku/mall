package com.noinch.mall.springboot.starter.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class EsClientTemplate {

    private ElasticsearchClient esClient;

    /**
     * 保存/更新文档
     */
    public <T> void saveOrUpdate(String index, String id, T document) {
        try {
            IndexRequest<T> request = IndexRequest.of(i -> i
                    .index(index)
                    .id(id)
                    .document(document)
            );
            esClient.index(request);
        } catch (IOException e) {
            log.error("ES save error, index: {}, id: {}", index, id, e);
            throw new RuntimeException("ES操作异常", e);
        }
    }

    /**
     * 根据 ID 删除单个文档
     */
    public boolean deleteById(String index, String id) {
        try {
            DeleteResponse response = esClient.delete(d -> d
                    .index(index)
                    .id(id)
            );
            // 返回结果可能是 'deleted' 或 'not_found'
            return "deleted".equalsIgnoreCase(response.result().jsonValue());
        } catch (IOException e) {
            log.error("ES delete error, index: {}, id: {}", index, id, e);
            throw new RuntimeException("删除文档失败", e);
        }
    }

    /**
     * 根据 ID 获取文档
     */
    public <T> T getById(String index, String id, Class<T> clazz) {
        try {
            GetResponse<T> response = esClient.get(g -> g.index(index).id(id), clazz);
            return response.found() ? response.source() : null;
        } catch (IOException e) {
            log.error("ES get error", e);
            throw new RuntimeException("获取文档失败", e);
        }
    }

    /**
     * 简单关键字搜索封装 (支持分页)
     */
    public <T> List<T> search(String index, String field, String keyword, int page, int size, Class<T> clazz) {
        try {
            SearchResponse<T> response = esClient.search(s -> s
                    .index(index)
                    .from((page - 1) * size)
                    .size(size)
                    .query(q -> q.match(m -> m.field(field).query(keyword))
                ), clazz);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("ES search error", e);
            throw new RuntimeException("搜索失败", e);
        }
    }

    /**
     * 暴露原生 Client，作为“逃生舱”，防止封装不足
     */
    public ElasticsearchClient getNativeClient() {
        return this.esClient;
    }

}
