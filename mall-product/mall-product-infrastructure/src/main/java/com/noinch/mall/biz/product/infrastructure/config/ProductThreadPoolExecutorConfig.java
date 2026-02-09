package com.noinch.mall.biz.product.infrastructure.config;

import com.noinch.mall.biz.product.infrastructure.config.properties.ProductThreadPoolExecutorProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableConfigurationProperties(ProductThreadPoolExecutorProperties.class)
public class ProductThreadPoolExecutorConfig {

    @Bean(name = "product-thread-factory")
    public ThreadFactory threadFactory() {
        return new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "mall-product-thread-" + threadNumber.getAndIncrement());
            }
        };
    }

    @Bean(name = "product-executor")
    public ExecutorService productThreadPoolExecutor(ProductThreadPoolExecutorProperties properties, @Qualifier("product-thread-factory") ThreadFactory threadFactory) {
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