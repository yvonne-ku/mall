package com.noinch.mall.biz.message.infrastructure.config;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.noinch.mall.biz.message.infrastructure.config.properties.AliyunSmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliyunSmsProperties.class)
public class AliyunSmsConfig {

    @Bean
    public Config config(AliyunSmsProperties properties) {
        return new Config()
                .setAccessKeyId(properties.getAccessKeyId())
                .setAccessKeySecret(properties.getAccessKeySecret())
                .setEndpoint(properties.getEndpoint());
    }

    @Bean
    public Client client(Config config) throws Exception {
        return new Client(config);
    }
}
