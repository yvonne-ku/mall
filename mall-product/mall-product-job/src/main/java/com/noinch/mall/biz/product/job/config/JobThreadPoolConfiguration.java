
package com.noinch.mall.biz.product.job.config;

import cn.hippo4j.core.executor.DynamicThreadPool;
import cn.hippo4j.core.executor.support.ThreadPoolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Xxl-Job 线程池配置类
 *
 */
@Configuration
public class JobThreadPoolConfiguration {
    
    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor productSkuInitSyncThreadPoolExecutor() {
        String productSkuInitSyncThreadPoolId = "product-sku-init-sync-executor";
        return ThreadPoolBuilder.builder()
                .threadPoolId(productSkuInitSyncThreadPoolId)
                .threadFactory(productSkuInitSyncThreadPoolId)
                .dynamicPool()
                .build();
    }
}
