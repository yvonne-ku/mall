package com.noinch.mall.biz.message.application.service.impl;

import com.noinch.mall.biz.message.application.req.PhoneSendCommand;
import com.noinch.mall.biz.message.application.req.PhoneVerifyCommand;
import com.noinch.mall.biz.message.application.resp.PhoneSendRespDTO;
import com.noinch.mall.biz.message.application.service.AliyunSmsService;
import com.noinch.mall.biz.message.application.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final AliyunSmsService aliyunSmsService;

    @Override
    public PhoneSendRespDTO phoneMessageSend(PhoneSendCommand phoneSendCommand) {
        return aliyunSmsService.sendSms(phoneSendCommand.getPhone());
    }

    @Override
    public Boolean phoneMessageVerify(PhoneVerifyCommand phoneVerifyCommand) {
        return aliyunSmsService.verifySms(phoneVerifyCommand.getPhone(), phoneVerifyCommand.getVerifyCode());
    }
}
