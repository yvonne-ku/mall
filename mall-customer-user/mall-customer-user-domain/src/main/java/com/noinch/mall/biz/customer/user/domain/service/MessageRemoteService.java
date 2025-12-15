package com.noinch.mall.biz.customer.user.domain.service;

public interface MessageRemoteService {

    Boolean sendSms(String phone);

    Boolean verifySms(String phone, String verifyCode);
}
