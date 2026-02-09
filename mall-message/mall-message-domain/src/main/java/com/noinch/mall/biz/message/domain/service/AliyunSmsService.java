package com.noinch.mall.biz.message.domain.service;

public interface AliyunSmsService {

    Boolean sendSms(String phone);

    Boolean verifySms(String phone, String verifyCode);
}
