
package com.noinch.mall.biz.product.job.config;

import com.noinch.mall.biz.product.infrastructure.config.properties.ProductThreadPoolExecutorProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Xxl-Job 线程池配置类
 *
 */
@Configuration
public class JobThreadPoolExecutorConfig {

    @Bean(name = "product-job-thread-factory")
    public ThreadFactory threadFactory() {
        return new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "mall-product-job-thread-" + threadNumber.getAndIncrement());
            }
        };
    }

    @Bean(name = "product-sku-init-sync-executor")
    public ExecutorService productSkuInitSyncThreadPoolExecutor(ProductThreadPoolExecutorProperties properties, @Qualifier("product-job-thread-factory") ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getQueueCapacity()),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
