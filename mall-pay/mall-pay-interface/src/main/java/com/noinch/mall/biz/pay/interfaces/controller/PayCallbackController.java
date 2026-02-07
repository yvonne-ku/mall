

package com.noinch.mall.biz.pay.interfaces.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.web.Results;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.pay.application.convert.PayCallbackRequestConvert;
import com.noinch.mall.biz.pay.application.req.PayCallbackCommand;
import com.noinch.mall.biz.pay.application.service.PayService;
import com.noinch.mall.biz.pay.domain.base.PayCallbackRequest;
import com.noinch.mall.biz.pay.domain.common.PayChannelEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付结果回调
 *
 */
@RestController
@RequiredArgsConstructor
public class PayCallbackController {
    
    private final PayService payService;
    
    @Operation(description = "支付宝回调，调用支付宝支付后，支付宝会调用此接口发送支付结果")
    @PostMapping("/api/pay-service/callback/alipay")
    public Result<Void> callbackAlipay(@RequestParam Map<String, Object> requestParam) {
        PayCallbackCommand payCallbackCommand = BeanUtil.mapToBean(requestParam, PayCallbackCommand.class, true, CopyOptions.create());
        payCallbackCommand.setChannel(PayChannelEnum.ALI_PAY.name());
        payCallbackCommand.setOrderRequestId(requestParam.get("out_trade_no").toString());
        payCallbackCommand.setGmtPayment(DateUtil.parse(requestParam.get("gmt_payment").toString()));
        PayCallbackRequest payCallbackRequest = PayCallbackRequestConvert.command2PayCallbackRequest(payCallbackCommand);
        payService.callback(payCallbackRequest);
        return Results.success();
    }
}
