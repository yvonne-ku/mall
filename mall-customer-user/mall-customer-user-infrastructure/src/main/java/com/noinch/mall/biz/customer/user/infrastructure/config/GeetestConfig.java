package com.noinch.mall.biz.customer.user.infrastructure.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "geetest")
public class GeetestConfig {

    @Value("${geetest.enabled:false}")
    private boolean enabled  = false;

    @Value("${geetest.captcha-id}")
    private String captchaId;

    @Value("${geetest.private-key}")
    private String privateKey;
}
