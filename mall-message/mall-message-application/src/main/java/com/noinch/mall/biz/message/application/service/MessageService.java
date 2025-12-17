package com.noinch.mall.biz.message.application.service;

import com.noinch.mall.biz.message.application.req.PhoneSendCommand;
import com.noinch.mall.biz.message.application.req.PhoneVerifyCommand;
import com.noinch.mall.biz.message.application.resp.PhoneSendRespDTO;

public interface MessageService {

    PhoneSendRespDTO phoneMessageSend(PhoneSendCommand phoneSendCommand);

    Boolean phoneMessageVerify(PhoneVerifyCommand phoneVerifyCommand);
}
