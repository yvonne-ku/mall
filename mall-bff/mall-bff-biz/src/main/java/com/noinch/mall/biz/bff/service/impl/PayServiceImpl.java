package com.noinch.mall.biz.bff.service.impl;

import com.noinch.mall.biz.bff.dto.req.adapter.PayAdapterReqDTO;
import com.noinch.mall.biz.bff.remote.PayRemoteService;
import com.noinch.mall.biz.bff.remote.req.PayReqDTO;
import com.noinch.mall.biz.bff.remote.resp.PayRespDTO;
import com.noinch.mall.biz.bff.service.PayService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class PayServiceImpl implements PayService {

    private final PayRemoteService payRemoteService;

    @Override
    public PayRespDTO commonPay(PayAdapterReqDTO requestParam) {
        PayReqDTO payReqDTO = new PayReqDTO();
        payReqDTO.setOrderSn(requestParam.getOrderId());
        payReqDTO.setTotalAmount(requestParam.getMoney());
        payReqDTO.setChannel(requestParam.getChannel());
        payReqDTO.setTradeType(requestParam.getTradeType());
        return payRemoteService.pay(payReqDTO).getData();
    }

    @Override
    public void callbackAlipay(Map<String, Object> requestParam) {
        payRemoteService.callbackAlipay(requestParam);
    }
}
