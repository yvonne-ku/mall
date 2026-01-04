package com.noinch.mall.springboot.starter.elasticsearch.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * 分布式缓存配置 */
@Data
@ConfigurationProperties(prefix = ElasticsearchProperties.PREFIX)
public class ElasticsearchProperties {

    public static final String PREFIX = "mall.elasticsearch";

    private String scheme = "http";

    private String host = "localhost";

    private int port = 9200;

    private String username;

    private String password;

    private Duration socketTimeout = Duration.ofSeconds(10);

    private Duration connectTimeout = Duration.ofSeconds(5);

    private Ssl ssl = new Ssl();

    @Data
    public static class Ssl {

        /**
         * 是否启用 SSL
         */
        private boolean enabled = false;

        /**
         * 证书授权机构文件路径
         */
        private String caCertificatePath;
    }

}
