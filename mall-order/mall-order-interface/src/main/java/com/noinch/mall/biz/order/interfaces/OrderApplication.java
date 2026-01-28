package com.noinch.mall.biz.order.interfaces;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableDiscoveryClient
//@EnableTransactionManagement(proxyTargetClass = true)
@EnableFeignClients("com.noinch.mall.biz.order.infrastructure.remote")
@SpringBootApplication(scanBasePackages = "com.noinch.mall.biz.order")
@MapperScan("com.noinch.mall.biz.order.infrastructure.dao")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
