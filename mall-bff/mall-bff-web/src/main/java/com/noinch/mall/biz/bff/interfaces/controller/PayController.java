package com.noinch.mall.biz.bff.interfaces.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import com.noinch.mall.biz.bff.common.ResultT;
import com.noinch.mall.biz.bff.dto.req.adapter.PayAdapterReqDTO;
import com.noinch.mall.biz.bff.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付控制层
 */
@RestController("bffPayController")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @Operation(description = "公共支付接口对接常用支付方式，比如：支付宝、微信以及银行卡等")
    @PostMapping("/member/pay")
    public ResultT<Void> pay(@RequestBody PayAdapterReqDTO requestParam) {
        payService.commonPay(requestParam);
        return ResultT.success();
    }

//    @Operation(description = "支付宝回调，调用支付宝支付后，支付宝会调用此接口发送支付结果")
//    @PostMapping("/member/pay/callback/alipay")
//    public void callbackAlipay(@RequestParam Map<String, Object> requestParam) {
//        PayCallbackCommand payCallbackCommand = BeanUtil.mapToBean(requestParam, PayCallbackCommand.class, true, CopyOptions.create());
//        payCallbackCommand.setChannel(PayChannelEnum.ALI_PAY.name());
//        payCallbackCommand.setOrderRequestId(requestParam.get("out_trade_no").toString());
//        payCallbackCommand.setGmtPayment(DateUtil.parse(requestParam.get("gmt_payment").toString()));
//        PayCallbackRequest payCallbackRequest = PayCallbackRequestConvert.command2PayCallbackRequest(payCallbackCommand);
//        payService.callback(payCallbackRequest);
//    }
}
