
package com.noinch.mall.biz.pay.application.service;

import com.noinch.mall.biz.pay.application.resp.CheckPaymentStatusRespDTO;
import com.noinch.mall.biz.pay.application.resp.PayRespDTO;
import com.noinch.mall.biz.pay.domain.base.PayCallbackRequest;
import com.noinch.mall.biz.pay.domain.base.PayRequest;

/**
 * 支付服务接口
 *
 */
public interface PayService {
    
    /**
     * 公共支付，对接支付宝、微信等常用支付方式
     *
     * @param requestParam 支付请求入参
     * @return 支付返回结果
     */
    PayRespDTO commonPay(PayRequest requestParam);


    /**
     * 查找支付状态
     * @param orderSn 订单号
     * @return 返回支付状态
     */
    CheckPaymentStatusRespDTO checkPaymentStatus(String orderSn);

    /**
     * 对接三方支付平台支付结果回调
     *
     * @param requestParam 支付回调请求入参
     */
    void callback(PayCallbackRequest requestParam);
}
