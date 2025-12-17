package com.noinch.mall.springboot.starter.distributedid.config;

import com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.workid.LocalRedisWorkIdChoose;
import com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.workid.RandomWorkIdChoose;
import com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.workid.WorkIdChooser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式 ID 自动装配
 * */
@Configuration
public class DistributedIdAutoConfiguration {

    /**
     * 本地 Redis 构建雪花 WorkId 选择器
     */
    @Bean
    @ConditionalOnProperty("spring.redis.host")
    public LocalRedisWorkIdChoose redisWorkIdChoose() {
        return new LocalRedisWorkIdChoose();
    }

    /**
     * 随机数构建雪花 WorkId 选择器。如果项目未使用 Redis，使用该选择器
     */
    @Bean
    @ConditionalOnMissingBean(LocalRedisWorkIdChoose.class)
    public RandomWorkIdChoose randomWorkIdChoose() {
        return new RandomWorkIdChoose();
    }

    /**
     * 雪花算法初始化器
     */
    @Bean
    public SnowflakeInitializer snowflakeInitializer(WorkIdChooser workIdChooser) {
        SnowflakeInitializer initializer = new SnowflakeInitializer(workIdChooser);
        initializer.initializeSnowflake();
        return initializer;
    }
}
