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

    private String accessKeyId;

    private String accessKeySecret;

    /**
     * 短信服务端地址
     */
    private String endpoint;

    /**
     * 短信签名名称
     */
    private String signName;

    /**
     * 默认短信模板编码
     */
    private String templateCode;

    /**
     * 过期时间（分钟）
     */
    private Integer expireMinutes = 5;

    /**
     * 让阿里云自动生成验证码
     */
    private String defaultVerifyCode = "##code##";
}
