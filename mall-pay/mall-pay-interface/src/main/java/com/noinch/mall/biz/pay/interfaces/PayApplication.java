package com.noinch.mall.biz.pay.interfaces;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.noinch.mall.biz.pay.infrastructure.dao")
@SpringBootApplication(scanBasePackages = "com.noinch.mall.biz.pay")
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

}
