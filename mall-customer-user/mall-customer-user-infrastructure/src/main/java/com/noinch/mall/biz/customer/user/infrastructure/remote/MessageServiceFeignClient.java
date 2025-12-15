package com.noinch.mall.biz.customer.user.infrastructure.remote;

import com.noinch.mall.biz.customer.user.domain.dto.PhoneSendReqDTO;
import com.noinch.mall.biz.customer.user.domain.dto.PhoneVerifyReqDTO;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mall-message-service", url = "${mall.message-service.url}")
public interface MessageServiceFeignClient {

    @PostMapping("/api/message/send/phone")
    Result<Boolean> sendMessage(@RequestBody PhoneSendReqDTO command);

    @PostMapping("/api/message/verify/phone")
    Result<Boolean> verifyPhoneMessage(@RequestBody PhoneVerifyReqDTO phoneVerifyCommand);
}
