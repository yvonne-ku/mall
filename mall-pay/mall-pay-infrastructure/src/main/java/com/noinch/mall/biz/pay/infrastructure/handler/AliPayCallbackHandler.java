

package com.noinch.mall.biz.pay.infrastructure.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.pay.domain.aggregate.Pay;
import com.noinch.mall.biz.pay.domain.base.AliPayCallbackRequest;
import com.noinch.mall.biz.pay.domain.base.PayCallbackRequest;
import com.noinch.mall.biz.pay.domain.common.PayChannelEnum;
import com.noinch.mall.biz.pay.domain.repository.PayRepository;
import com.noinch.mall.biz.pay.infrastructure.handler.base.AbstractPayCallbackHandler;
import com.noinch.mall.springboot.starter.designpattern.strategy.AbstractExecuteStrategy;
import org.springframework.stereotype.Service;

/**
 * 阿里支付回调组件
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public final class AliPayCallbackHandler extends AbstractPayCallbackHandler implements AbstractExecuteStrategy<PayCallbackRequest, Void> {
    
    private final PayRepository payRepository;
    
    @Override
    public void callback(PayCallbackRequest payCallbackRequest) {
        AliPayCallbackRequest aliPayCallbackRequest = payCallbackRequest.getAliPayCallbackRequest();
        Pay pay = Pay.builder()
                .status(aliPayCallbackRequest.getTradeStatus())
                .payAmount(aliPayCallbackRequest.getBuyerPayAmount())
                .tradeNo(aliPayCallbackRequest.getTradeNo())
                .gmtPayment(aliPayCallbackRequest.getGmtPayment())
                .orderRequestId(aliPayCallbackRequest.getOrderRequestId())
                .build();
        payRepository.callbackPay(pay);
    }
    
    @Override
    public String mark() {
        return PayChannelEnum.ALI_PAY.name();
    }
    
    public void execute(PayCallbackRequest requestParam) {
        callback(requestParam);
    }
}
