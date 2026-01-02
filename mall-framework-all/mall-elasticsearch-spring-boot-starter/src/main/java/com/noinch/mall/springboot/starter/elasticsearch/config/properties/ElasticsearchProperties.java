package com.noinch.mall.springboot.starter.elasticsearch.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

    private String socketTimeout = "10s";

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
        private String certificateAuthorities;

        /**
         * 客户端证书文件路径
         */
        private String certificate;

        /**
         * 客户端私钥文件路径
         */
        private String certificateKey;

        /**
         * 密码
         */
        private String password;

        /**
         * 是否验证主机名
         */
        private boolean verificationMode = false;
    }

}
