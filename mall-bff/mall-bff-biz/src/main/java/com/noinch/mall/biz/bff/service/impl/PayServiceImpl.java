package com.noinch.mall.biz.bff.service.impl;

import com.noinch.mall.biz.bff.dto.req.adapter.PayAdapterReqDTO;
import com.noinch.mall.biz.bff.remote.PayRemoteService;
import com.noinch.mall.biz.bff.remote.req.PayReqDTO;
import com.noinch.mall.biz.bff.service.PayService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PayServiceImpl implements PayService {

    private final PayRemoteService payRemoteService;

    @Override
    public void commonPay(PayAdapterReqDTO requestParam) {
        PayReqDTO payReqDTO = new PayReqDTO();

        payRemoteService.pay(payReqDTO);

    }
}
