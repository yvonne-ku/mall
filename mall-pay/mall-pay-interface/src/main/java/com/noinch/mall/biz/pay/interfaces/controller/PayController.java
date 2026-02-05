
package com.noinch.mall.biz.pay.interfaces.controller;

import cn.hutool.core.bean.BeanUtil;
import com.noinch.mall.biz.pay.application.req.PayReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.pay.application.convert.PayRequestConvert;
import com.noinch.mall.biz.pay.application.req.PayCommand;
import com.noinch.mall.biz.pay.application.resp.PayRespDTO;
import com.noinch.mall.biz.pay.application.service.PayService;
import com.noinch.mall.biz.pay.domain.base.PayRequest;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.web.Results;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付接口
 *
 */
@RestController
@RequiredArgsConstructor
public class PayController {
    
    private final PayService payService;
    
    @Operation(description = "公共支付接口对接常用支付方式，比如：支付宝、微信以及银行卡等")
    @PostMapping("/api/pay-service")
    public Result<PayRespDTO> pay(@RequestBody PayReqDTO requestParam) {
        PayCommand command = new PayCommand();
        BeanUtil.copyProperties(requestParam, command);
        PayRequest payRequest = PayRequestConvert.command2PayRequest(command);
        PayRespDTO result = payService.commonPay(payRequest);
        return Results.success(result);
    }
}
