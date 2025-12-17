package com.noinch.mall.springboot.starter.jwt.config;

import com.noinch.mall.springboot.starter.jwt.toolkit.TokenUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT自动配置类
 */
@Configuration
@ConditionalOnClass(TokenUtil.class)
@EnableConfigurationProperties(JwtProperties.class)
public class JwtAutoConfiguration {

    /**
     * 创建TokenUtil Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenUtil tokenUtil(JwtProperties jwtProperties) {
        return new TokenUtil(jwtProperties);
    }
}