package com.noinch.mall.springboot.starter.base.config;


import com.noinch.mall.springboot.starter.base.ApplicationContextHolder;
import com.noinch.mall.springboot.starter.base.init.ApplicationContentPostProcessor;
import com.noinch.mall.springboot.starter.base.safe.FastJsonSafeMode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 应用基础自动装配
 */
public class ApplicationBaseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextHolder congoApplicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContentPostProcessor congoApplicationContentPostProcessor() {
        return new ApplicationContentPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mall.fastjson.safe-mode", havingValue = "true")
    public FastJsonSafeMode mallFastJsonSafeMode() {
        return new FastJsonSafeMode();
    }
}
