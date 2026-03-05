package com.noinch.mall.springboot.starter.web.config;

import com.noinch.mall.springboot.starter.web.interceptor.SeataFeignHandlerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class SeataInterceptorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SeataFeignHandlerInterceptor seataHandlerInterceptor() {
        return new SeataFeignHandlerInterceptor();
    }
}
