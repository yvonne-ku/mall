package com.noinch.mall.biz.message.infrastructure.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Aliyun 短信配置
 */
@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsProperties {

    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret}")
    private String accessKeySecret;

    /**
     * 短信服务端地址
     */
    @Value("${aliyun.sms.endpoint}")
    private String endpoint;

    /**
     * 短信签名名称
     */
    @Value("${aliyun.sms.sign-name}")
    private String signName;

    /**
     * 默认短信模板编码
     */
    @Value("${aliyun.sms.template-code}")
    private String templateCode;

    /**
     * 过期时间（分钟）
     */
    @Value("${aliyun.sms.expire-minutes}")
    private Integer expireMinutes = 5;

    /**
     * 让阿里云自动生成验证码
     */
    @Value("${aliyun.sms.default-verify-code}")
    private String defaultVerifyCode = "##code##";
}
