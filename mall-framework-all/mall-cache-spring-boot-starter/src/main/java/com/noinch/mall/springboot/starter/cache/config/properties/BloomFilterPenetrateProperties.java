package com.noinch.mall.springboot.starter.cache.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存穿透布隆过滤器配置类
 * */
@Data
@ConfigurationProperties(prefix = BloomFilterPenetrateProperties.PREFIX)
public class BloomFilterPenetrateProperties {
    
    public static final String PREFIX = "mall.cache.redis.bloom-filter.default";
    
    /**
     * 布隆过滤器默认实例名称
     */
    private String name = "cache_penetration_bloom_filter";
    
    /**
     * 预计要向布隆过滤器插入多少元素，这个值越大，所需的位数越多，误判率越低
     */
    private Long expectedInsertions = 64000L;
    
    /**
     * 预期错误概率，默认值为 0.03，即期望 3% 的错误率
     */
    private Double falseProbability = 0.03D;
}
