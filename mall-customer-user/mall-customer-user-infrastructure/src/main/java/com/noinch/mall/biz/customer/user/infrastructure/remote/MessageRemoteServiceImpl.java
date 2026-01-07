package com.noinch.mall.biz.customer.user.infrastructure.remote;

import com.noinch.mall.biz.customer.user.domain.service.MessageRemoteService;
import com.noinch.mall.biz.customer.user.domain.dto.PhoneSendReqDTO;
import com.noinch.mall.biz.customer.user.domain.dto.PhoneVerifyReqDTO;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageRemoteServiceImpl implements MessageRemoteService {

    private final MessageServiceFeignClient messageServiceFeignClient;

    @Override
    public Boolean sendSms(String phone) {
        PhoneSendReqDTO phoneSendReqDTO = PhoneSendReqDTO.builder()
                .phone(phone)
                .build();
        Result<Boolean> result = messageServiceFeignClient.sendMessage(phoneSendReqDTO);
        return result.getData();
    }

    @Override
    public Boolean verifySms(String phone, String verifyCode) {
        PhoneVerifyReqDTO phoneVerifyReqDTO = PhoneVerifyReqDTO.builder()
                .phone(phone)
                .verifyCode(verifyCode)
                .build();
        Result<Boolean> result = messageServiceFeignClient.verifyPhoneMessage(phoneVerifyReqDTO);
        return result.getData();
    }
}
