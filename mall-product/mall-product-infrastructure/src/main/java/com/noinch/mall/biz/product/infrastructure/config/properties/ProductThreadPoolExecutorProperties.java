package com.noinch.mall.biz.product.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "product.thread-pool.default-executor")
public class ProductThreadPoolExecutorProperties {

    private int corePoolSize = 20;

    private int maximumPoolSize = 40;

    private int keepAliveTime = 9999;

    private int queueCapacity = 4096;
}
