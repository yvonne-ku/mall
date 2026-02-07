package com.noinch.mall.biz.bff.service;

import com.noinch.mall.biz.bff.dto.req.adapter.PayAdapterReqDTO;
import com.noinch.mall.biz.bff.remote.resp.PayRespDTO;

import java.util.Map;

public interface PayService {

    /**
     * 检查支付状态
     */
    String checkPaymentStatus(String orderSn);

    /**
     * 普通支付
     */
    PayRespDTO commonPay(PayAdapterReqDTO requestParam);

    /**
     * 支付宝回调
     */
    void callbackAlipay(Map<String, Object> requestParam);
}
