

package com.noinch.mall.biz.pay.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝配置
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = AliPayProperties.PREFIX)
public class AliPayProperties {
    
    public static final String PREFIX = "pay.alipay";

    /**
     * 支付结果回调地址
     */
    private String notifyUrl;

    /**
     * 网关地址
     */
    private String serverUrl;

    /**
     * 开放平台上创建的应用的 ID
     */
    private String appId;

    /**
     * 商户私钥
     */
    private String privateKey;
    
    /**
     * 支付宝公钥字符串（公钥模式下设置，证书模式下无需设置）
     */
    private String alipayPublicKey;
    
    /**
     * 报文格式，推荐：json
     */
    private String format;
    
    /**
     * 字符串编码，推荐：utf-8
     */
    private String charset;
    
    /**
     * 签名算法类型，推荐：RSA2
     */
    private String signType;
}
