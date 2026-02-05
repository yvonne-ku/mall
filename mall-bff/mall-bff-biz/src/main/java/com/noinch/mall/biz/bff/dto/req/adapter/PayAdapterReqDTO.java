package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 支付请求对象
 */
@Data
public class PayAdapterReqDTO {

    /**
     * 名称
     */
    private String nickName;

    /**
     * 金额
     */
    private String money;

    /**
     * 用户留言
     */
    private String info;

    /**
     * 通知邮箱
     */
    private String email;

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
    private String payType;

}
