package com.noinch.mall.biz.pay.infrastructure.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.noinch.mall.biz.pay.infrastructure.config.properties.AliPayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliPayConfig {

    @Bean
    public AlipayConfig alipayConfig(AliPayProperties aliPayProperties) {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(aliPayProperties.getServerUrl());
        alipayConfig.setAppId(aliPayProperties.getAppId());
        alipayConfig.setPrivateKey(aliPayProperties.getPrivateKey());
        alipayConfig.setAlipayPublicKey(aliPayProperties.getAlipayPublicKey());
        alipayConfig.setFormat(aliPayProperties.getFormat());
        alipayConfig.setCharset(aliPayProperties.getCharset());
        alipayConfig.setSignType(aliPayProperties.getSignType());
        return alipayConfig;
    }

    @Bean
    public AlipayClient alipayClient(AlipayConfig alipayConfig) throws AlipayApiException {
        return new DefaultAlipayClient(alipayConfig);
    }
}
