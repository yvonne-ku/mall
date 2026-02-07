package com.noinch.mall.biz.bff.service.impl;

import com.noinch.mall.biz.bff.dto.req.adapter.PayAdapterReqDTO;
import com.noinch.mall.biz.bff.remote.PayRemoteService;
import com.noinch.mall.biz.bff.remote.req.PayReqDTO;
import com.noinch.mall.biz.bff.remote.resp.CheckPaymentStatusRespDTO;
import com.noinch.mall.biz.bff.remote.resp.PayRespDTO;
import com.noinch.mall.biz.bff.service.PayService;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class PayServiceImpl implements PayService {

    private final PayRemoteService payRemoteService;

    @Override
    public String checkPaymentStatus(String orderSn) {
        Result<CheckPaymentStatusRespDTO> result = payRemoteService.checkPaymentStatus(orderSn);
        if (!result.isSuccess() || result.getData() == null) {
            throw new ServiceException("调用支付服务查询支付状态失败");
        }
        return result.getData().getStatus();
    }

    @Override
    public PayRespDTO commonPay(PayAdapterReqDTO requestParam) {
        PayReqDTO payReqDTO = new PayReqDTO();
        payReqDTO.setOrderSn(requestParam.getOrderId());
        payReqDTO.setTotalAmount(requestParam.getMoney());
        payReqDTO.setChannel(requestParam.getChannel());
        payReqDTO.setTradeType(requestParam.getTradeType());

        Result<PayRespDTO> result = payRemoteService.pay(payReqDTO);
        if (!result.isSuccess() || result.getData() == null) {
            throw new ServiceException("调用支付服务支付失败");
        }
        return result.getData();
    }

    @Override
    public void callbackAlipay(Map<String, Object> requestParam) {
        Result<Void> result = payRemoteService.callbackAlipay(requestParam);
        if (!result.isSuccess()) {
            throw new ServiceException("调用支付服务支付宝回调失败");
        }
    }
}
