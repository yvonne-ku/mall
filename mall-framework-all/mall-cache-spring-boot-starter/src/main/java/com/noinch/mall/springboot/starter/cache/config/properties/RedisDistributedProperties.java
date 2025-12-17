package com.noinch.mall.springboot.starter.cache.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * 分布式缓存配置 */
@Data
@ConfigurationProperties(prefix = RedisDistributedProperties.PREFIX)
public class RedisDistributedProperties {
    
    public static final String PREFIX = "mall.cache.redis";
    
    /**
     * Key 前缀
     */
    private String prefix = "mall:";
    
    /**
     * Key 前缀字符集
     */
    private String prefixCharset = "UTF-8";
    
    /**
     * 默认超时时间
     */
    private Long valueTimeout = 30000L;
    
    /**
     * 时间单位
     */
    private TimeUnit valueTimeUnit = TimeUnit.MILLISECONDS;
}
