package com.noinch.mall.biz.cart.interfaces;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@MapperScan("com.noinch.mall.biz.cart.infrastructure.dao")
@SpringBootApplication(scanBasePackages = "com.noinch.mall.biz.cart")
public class CartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }

}
