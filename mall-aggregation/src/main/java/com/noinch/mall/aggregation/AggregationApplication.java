package com.noinch.mall.aggregation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * 在开发阶段，可以通过启动一个Aggregation模块快速启动整个系统
 * 简化了测试环境的搭建，不需要部署多个独立服务
 * 便于集成测试和端到端测试
 */
//@EnableCreateCacheAnnotation
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