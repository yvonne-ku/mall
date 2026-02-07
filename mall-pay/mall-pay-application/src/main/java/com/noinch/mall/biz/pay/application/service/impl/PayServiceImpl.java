
package com.noinch.mall.biz.pay.application.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.noinch.mall.biz.pay.application.resp.CheckPaymentStatusRespDTO;
import com.noinch.mall.biz.pay.domain.common.TradeStatusEnum;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.pay.application.resp.PayRespDTO;
import com.noinch.mall.biz.pay.application.service.PayService;
import com.noinch.mall.biz.pay.domain.aggregate.Pay;
import com.noinch.mall.biz.pay.domain.base.PayCallbackRequest;
import com.noinch.mall.biz.pay.domain.base.PayRequest;
import com.noinch.mall.biz.pay.domain.base.PayResponse;
import com.noinch.mall.biz.pay.domain.repository.PayRepository;
import com.noinch.mall.springboot.starter.designpattern.strategy.AbstractStrategyChoose;
import org.springframework.stereotype.Service;

/**
 * 支付接口
 *
 */
@Service
@RequiredArgsConstructor
public final class PayServiceImpl implements PayService {
    
    private final AbstractStrategyChoose abstractStrategyChoose;
    
    private final PayRepository payRepository;

    @Override
    public CheckPaymentStatusRespDTO checkPaymentStatus(String orderSn) {
        Pay pay = payRepository.findPayByOrderSn(orderSn);
        CheckPaymentStatusRespDTO dto = new CheckPaymentStatusRespDTO();
        dto.setOrderSn(orderSn);
        dto.setStatus(pay.getStatus());
        return dto;
    }

    @Override
    public PayRespDTO commonPay(PayRequest requestParam) {
        // 执行支付
        PayResponse result = abstractStrategyChoose.chooseAndExecuteResp(requestParam.buildMark(), requestParam);

        // 数据库层面创建 PayDO
        Pay pay = new Pay();
        BeanUtil.copyProperties(requestParam, pay);
        pay.setStatus(TradeStatusEnum.WAIT_BUYER_PAY.name());
        payRepository.createPay(pay);

        // 返回
        PayRespDTO dto = new PayRespDTO();
        BeanUtil.copyProperties(result, dto);
        return dto;
    }

    @Override
    public void callback(PayCallbackRequest requestParam) {
        // 执行支付回调
        abstractStrategyChoose.chooseAndExecute(requestParam.buildMark(), requestParam);
    }
}
