package com.noinch.mall.biz.bff.remote;

import com.noinch.mall.biz.bff.remote.req.PayReqDTO;
import com.noinch.mall.biz.bff.remote.resp.CheckPaymentStatusRespDTO;
import com.noinch.mall.biz.bff.remote.resp.PayRespDTO;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "pay-service")
//@FeignClient(value = "pay-service", url = "${mall.remote-url.pay-service:}")
public interface PayRemoteClient {


    @PostMapping("/api/pay-service/checkStatus/{orderSn}")
    Result<CheckPaymentStatusRespDTO> checkPaymentStatus(@PathVariable("orderSn") String orderSn);

    @PostMapping("/api/pay-service")
    Result<PayRespDTO> pay(@RequestBody PayReqDTO requestParam);

    @PostMapping("/api/pay-service/callback/alipay")
    Result<Void> callbackAlipay(@RequestParam Map<String, Object> requestParam);

}
