

package com.noinch.mall.biz.bff.interfaces.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付渠道枚举
 *
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {
    
    /**
     * 支付宝
     */
    ALI_PAY("Alipay"),

    /**
     * 微信支付
     */
    WX_PAY("Wechat"),

    /**
     * QQ 支付
     */
    QQ_PAY("QQPay");

    /**
     * 支付渠道字符串值
     */
    private final String channel;

}
