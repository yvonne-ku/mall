package com.noinch.mall.biz.message.application.service;

import com.noinch.mall.biz.message.application.req.PhoneSendCommand;
import com.noinch.mall.biz.message.application.req.PhoneVerifyCommand;

public interface MessageService {

    Boolean phoneMessageSend(PhoneSendCommand phoneSendCommand);

    Boolean phoneMessageVerify(PhoneVerifyCommand phoneVerifyCommand);
}
