package com.noinch.mall.biz.message.interfaces;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.noinch.mall.biz.message.infrastructure.dao.mapper")
@SpringBootApplication(scanBasePackages = "com.noinch.mall.biz.message")
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

}
