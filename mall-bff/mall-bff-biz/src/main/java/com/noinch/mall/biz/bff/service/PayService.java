package com.noinch.mall.biz.bff.service;

import com.noinch.mall.biz.bff.dto.req.adapter.PayAdapterReqDTO;

public interface PayService {

    /**
     * 普通支付
     */
    void commonPay(PayAdapterReqDTO requestParam);

//    void callbackPay(PayAdapterReqDTO payAdapterReqDTO);
}
