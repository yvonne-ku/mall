package com.noinch.mall.springboot.starter.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.noinch.mall.springboot.starter.elasticsearch.config.properties.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

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
        }

        // 配置 Ssl 上下文
        SSLContext sslContext = null;
        if (properties.getSsl().isEnabled()) {
            // 加载 CA 证书
            Certificate caCert;
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            String caCertificatePath = properties.getSsl().getCaCertificatePath();
            try (InputStream caInputStream = new ClassPathResource(caCertificatePath).getInputStream()) {
                caCert = certificateFactory.generateCertificate(caInputStream);

                // Keystore（密钥库）：存放你的私钥和证书，用于向别人证明自己的身份
                // Truststore（信任库）：存放你所信任的别人的证书，用于对比验证
                // 当你访问一个 HTTPS URL，对方会发给你一个证书，你的 java 程序回去 Truststore 中找看看这个证书的 CA 在不在你的信任名单中，如果不在，报 SSLHandshakeException

                // 创建一个空的信任库，并导入 CA 证书
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                trustStore.setCertificateEntry("elastic-ca", caCert);

                // 构建 SSL 上下文，使用包含 CA 证书的信任库
               sslContext = SSLContextBuilder.create()
                       .loadTrustMaterial(trustStore, null)
                       .build();
            }
        }

        final SSLContext finalSslContext = sslContext;
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            if (properties.getUsername() != null) {
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
            if (properties.getSsl().isEnabled() && finalSslContext != null) {
                httpClientBuilder.setSSLContext(finalSslContext);
                httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
            }
            return httpClientBuilder;
        });

        return builder.build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(ElasticsearchProperties properties) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        return new RestClientTransport(restClient(properties), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport elasticsearchTransport) {
        return new ElasticsearchClient(elasticsearchTransport);
    }

    @Bean
    public ElasticsearchOperations elasticsearchOperations(ElasticsearchClient elasticsearchClient) {
        return new ElasticsearchTemplate(elasticsearchClient);
    }
}
