
package com.noinch.mall.biz.product.interfaces.config;

import cn.hippo4j.core.executor.DynamicThreadPool;
import cn.hippo4j.core.executor.support.ThreadPoolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 */
@Configuration
public class ThreadPoolConfiguration {
    
    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor productThreadPoolExecutor() {
        String productThreadPoolId = "product-common-executor";
        return ThreadPoolBuilder.builder()
                .threadPoolId(productThreadPoolId)
                .threadFactory(productThreadPoolId)
                .dynamicPool()
                .build();
    }
}
