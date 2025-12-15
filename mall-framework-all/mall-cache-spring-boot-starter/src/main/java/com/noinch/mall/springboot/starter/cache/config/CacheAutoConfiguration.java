package com.noinch.mall.springboot.starter.cache.config;


import com.noinch.mall.springboot.starter.cache.RedisKeySerializer;
import com.noinch.mall.springboot.starter.cache.StringRedisTemplateProxy;
import com.noinch.mall.springboot.starter.cache.config.properties.BloomFilterPenetrateProperties;
import com.noinch.mall.springboot.starter.cache.config.properties.RedisDistributedProperties;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 缓存自动装配类
 */
@EnableConfigurationProperties({RedisDistributedProperties.class, BloomFilterPenetrateProperties.class})
@AutoConfigureAfter({RedisAutoConfiguration.class})
public class CacheAutoConfiguration {

    /**
     * 创建 Redis Key 序列化器，可自定义 Key Prefix
     */
    @Bean
    public RedisKeySerializer redisKeySerializer(RedisDistributedProperties redisDistributedProperties) {
        String prefix = redisDistributedProperties.getPrefix();
        String prefixCharset = redisDistributedProperties.getPrefixCharset();
        return new RedisKeySerializer(prefix, prefixCharset);
    }

    /**
     * 防止缓存穿透的布隆过滤器
     * 方法入参会由 Spring 自动寻找是否有对应的 Bean 进行注入，如果没有对应的 Bean，则会报错
     * ConditionalOnProperty 注解表示，只有当配置项 mall.cache.redis.bloom-filter.default.enabled 的值为 true，或者该配置项不存在时，才创建这个 Bean。
     */
    @Bean
    @ConditionalOnProperty(prefix = BloomFilterPenetrateProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
    public RBloomFilter<String> rBloomFilter(RedissonClient redissonClient, BloomFilterPenetrateProperties bloomFilterPenetrateProperties) {
        RBloomFilter<String> rBloomFilter = redissonClient.getBloomFilter(bloomFilterPenetrateProperties.getName());
        rBloomFilter.tryInit(bloomFilterPenetrateProperties.getExpectedInsertions(), bloomFilterPenetrateProperties.getFalseProbability());
        return rBloomFilter;
    }

    /**
     * 静态代理模式: Redis 客户端代理类增强
     */
    @Bean
    public StringRedisTemplateProxy stringRedisTemplateProxy(RedisDistributedProperties redisDistributedProperties,
                                                             RedisKeySerializer redisKeySerializer,
                                                             StringRedisTemplate stringRedisTemplate,
                                                             RedissonClient redissonClient) {
        stringRedisTemplate.setKeySerializer(redisKeySerializer);
        return new StringRedisTemplateProxy(stringRedisTemplate, redisDistributedProperties, redissonClient);
    }
}
