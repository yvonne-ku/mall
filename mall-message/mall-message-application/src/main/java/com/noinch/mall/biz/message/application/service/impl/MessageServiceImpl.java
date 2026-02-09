package com.noinch.mall.biz.message.application.service.impl;

import com.noinch.mall.biz.message.application.req.PhoneSendCommand;
import com.noinch.mall.biz.message.application.req.PhoneVerifyCommand;
import com.noinch.mall.biz.message.application.service.MessageService;
import com.noinch.mall.biz.message.domain.service.MessageDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageDomainService messageDomainService;

    @Override
    public Boolean phoneMessageSend(PhoneSendCommand phoneSendCommand) {
        return messageDomainService.sendSms(phoneSendCommand.getPhone());
    }

    @Override
    public Boolean phoneMessageVerify(PhoneVerifyCommand phoneVerifyCommand) {
        return messageDomainService.verifySms(phoneVerifyCommand.getPhone(), phoneVerifyCommand.getVerifyCode());
    }
}
