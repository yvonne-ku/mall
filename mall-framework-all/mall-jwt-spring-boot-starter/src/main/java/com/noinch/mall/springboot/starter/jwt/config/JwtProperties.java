package com.noinch.mall.springboot.starter.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT配置属性 */
@Data
@ConfigurationProperties(prefix = "mall.jwt")
public class JwtProperties {

    /**
     * JWT秘钥
     */
    private String secret = "";

    /**
     * 签发者
     */
    private String iss = "";

    /**
     * 算法
     */
    private String alg = "HS256";

    /**
     * 过期时间（秒）
     */
    private int expire = 3600;

    /**
     * 主题
     */
    private String subject = "login";
}