package com.noinch.mall.biz.bff.interfaces;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableDiscoveryClient
//@EnableCreateCacheAnnotation
//@EnableMethodCache(basePackages = "com.noinch.mall.bff")
@MapperScan("com.noinch.mall.biz.bff.dao.mapper")
@EnableFeignClients("com.noinch.mall.biz.bff.remote")
@SpringBootApplication(scanBasePackages = "com.noinch.mall.biz.bff")
public class BFFApplication {

    public static void main(String[] args) {
        SpringApplication.run(BFFApplication.class, args);
    }

}
