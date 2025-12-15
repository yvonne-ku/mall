
package com.noinch.mall.biz.customer.user.interfaces;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.noinch.mall.biz.customer.user.infrastructure.remote")
@MapperScan("com.noinch.mall.biz.customer.user.infrastructure.dao")
@SpringBootApplication(scanBasePackages = "com.noinch.mall.biz.customer.user")
public class CustomerUserApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CustomerUserApplication.class, args);
    }
}
