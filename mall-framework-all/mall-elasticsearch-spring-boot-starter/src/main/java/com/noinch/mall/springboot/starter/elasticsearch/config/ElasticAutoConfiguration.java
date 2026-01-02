package com.noinch.mall.springboot.starter.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.noinch.mall.springboot.starter.elasticsearch.EsClientTemplate;
import com.noinch.mall.springboot.starter.elasticsearch.config.properties.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * 缓存自动装配类
 */
@EnableConfigurationProperties({ElasticsearchProperties.class})
public class ElasticAutoConfiguration {

    @Bean
    public RestClient restClient(ElasticsearchProperties properties) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        RestClientBuilder builder = RestClient.builder(new HttpHost(properties.getHost(), properties.getPort(), properties.getScheme()));

        // 配置用户名密码
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (properties.getUsername() != null && properties.getPassword() != null) {
            // 构造用户名密码
            credentialsProvider.setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword())
            );
            // 配置用户名密码 builder
            builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        // 配置 Ssl 上下文
        if (properties.getSsl().isEnabled()) {
            KeyStore trustStore = KeyStore.getInstance("pkcs12");
            // 加载信任库
            try (InputStream trustStoreInputStream = new FileInputStream(properties.getSsl().getCertificateAuthorities())) {
                trustStore.load(trustStoreInputStream, properties.getSsl().getPassword().toCharArray());
            }
            // 构建 SSL 上下文
            SSLContextBuilder sslContextBuilder = SSLContextBuilder.create().loadTrustMaterial(trustStore, null);
            final SSLContext sslContext = sslContextBuilder.build();
            // 配置 SSL 上下文
            builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setSSLContext(sslContext));
        }

        return builder.build();
    }

    @Bean
    public ElasticsearchTransport elasticTransport(ElasticsearchProperties properties) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        return new RestClientTransport(restClient(properties), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticClient(ElasticsearchTransport elasticTransport) {
        return new ElasticsearchClient(elasticTransport);
    }

    @Bean
    public EsClientTemplate esClientTemplate(ElasticsearchClient elasticClient) {
        return new EsClientTemplate(elasticClient);
    }
}
