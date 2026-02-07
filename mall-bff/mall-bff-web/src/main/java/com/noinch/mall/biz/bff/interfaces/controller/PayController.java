package com.noinch.mall.biz.bff.interfaces.controller;


import com.noinch.mall.biz.bff.common.ResultT;
import com.noinch.mall.biz.bff.dto.req.adapter.PayAdapterReqDTO;
import com.noinch.mall.biz.bff.interfaces.common.PayChannelEnum;
import com.noinch.mall.biz.bff.interfaces.common.PayTradeTypeEnum;
import com.noinch.mall.biz.bff.remote.resp.PayRespDTO;
import com.noinch.mall.biz.bff.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    public ResultT<PayRespDTO> pay(HttpServletResponse httpResponse, @RequestBody PayAdapterReqDTO requestParam) throws IOException {
        PayRespDTO dto = payService.commonPay(requestParam);
        if (requestParam.getChannel().equals(PayChannelEnum.ALI_PAY.getChannel()) && requestParam.getTradeType().equals(PayTradeTypeEnum.NATIVE.name())) {
            httpResponse.setContentType("text/html;charset=utf-8");
            httpResponse.getWriter().write(dto.getBody());
            httpResponse.getWriter().flush();
            return null;
        }
        else if (requestParam.getChannel().equals(PayChannelEnum.ALI_PAY.getChannel()) && requestParam.getTradeType().equals(PayTradeTypeEnum.NATIVE_QR_CODE.name())) {
            return ResultT.success(dto);
        }
        return null;
    }

    @Operation(description = "支付宝回调，调用支付宝支付后，支付宝会调用此接口发送支付结果")
    @PostMapping("/member/pay/callback/alipay")
    public void callbackAlipay(@RequestParam Map<String, Object> requestParam) {
        payService.callbackAlipay(requestParam);
    }
}
