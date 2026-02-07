package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 支付请求对象
 */
@Data
public class PayAdapterReqDTO {

    /**
     * 金额
     */
    private String money;

    /**
     * 通知邮箱
     */
    private String email;

    /**
     * 用户留言
     */
    private String info;

    /**
     * 支付订单号
     */
    private String orderId;

    /**
     * 用户 id
     */
    private String userId;

    /**
     * 支付方式
     */
    private String channel;

    /**
     * 支付类型：网站、H5
     */
    private String tradeType;

}
