package com.noinch.mall.aggregation;

import cn.hippo4j.core.enable.EnableDynamicThreadPool;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDynamicThreadPool
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.noinch.mall.bff")
@MapperScan(value = {
        "com.noinch.mall.biz.*.infrastructure.dao.mapper",
        "com.noinch.mall.biz.customer.user.infrastructure.dao.mapper",
        "com.noinch.mall.biz.bff.dao.mapper"
})
@EnableFeignClients(value = {
        "com.noinch.mall.biz.bff.remote",
        "com.noinch.mall.biz.order.infrastructure.remote",
        "com.noinch.mall.biz.customer.user.infrastructure.remote"
})
@SpringBootApplication(scanBasePackages = {"com.noinch.mall",})
public class AggregationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AggregationApplication.class, args);
    }
}