package com.noinch.mall.biz.message.application.service;

import com.noinch.mall.biz.message.application.resp.PhoneSendRespDTO;

public interface AliyunSmsService {

    PhoneSendRespDTO sendSms(String phone);

    Boolean verifySms(String phone, String verifyCode);
}
