package com.noinch.mall.biz.message.domain.service.impl;

import com.noinch.mall.biz.message.domain.entity.MessageSend;
import com.noinch.mall.biz.message.domain.repository.MessageRepository;
import com.noinch.mall.biz.message.domain.service.AliyunSmsService;
import com.noinch.mall.biz.message.domain.service.MessageDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageDomainServiceImpl implements MessageDomainService {

    private final MessageRepository messageRepository;
    private final AliyunSmsService aliyunSmsService;

    @Override
    public Boolean sendSms(String phone) {
        Boolean res = aliyunSmsService.sendSms(phone);
        MessageSend send = MessageSend.builder()
                .receiver(phone)
                .msgType(0)
                .status(res)
                .build();
        messageRepository.save(send);
        return res;
    }

    @Override
    public Boolean verifySms(String phone, String verifyCode) {
        return aliyunSmsService.verifySms(phone, verifyCode);
    }
}
