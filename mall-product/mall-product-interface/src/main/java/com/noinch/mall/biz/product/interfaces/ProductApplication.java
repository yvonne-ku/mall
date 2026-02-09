package com.noinch.mall.biz.product.interfaces;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@EnableTask
//@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.noinch.mall.biz.product")
@MapperScan("com.noinch.mall.biz.product.infrastructure.dao.mapper")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
