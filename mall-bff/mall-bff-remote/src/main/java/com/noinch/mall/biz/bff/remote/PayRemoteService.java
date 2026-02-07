package com.noinch.mall.biz.bff.remote;

import com.noinch.mall.biz.bff.remote.req.PayReqDTO;
import com.noinch.mall.biz.bff.remote.resp.PayRespDTO;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "pay-service", url = "${mall.pay-service.url:}")
public interface PayRemoteService {

    /**
     * 商品订单下单
     */
    @PostMapping("/api/pay-service")
    Result<PayRespDTO> pay(@RequestBody PayReqDTO requestParam);

    @PostMapping("/api/pay-service/callback/alipay")
    void callbackAlipay(@RequestParam Map<String, Object> requestParam);
}
