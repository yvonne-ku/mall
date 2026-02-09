package com.noinch.mall.biz.message.infrastructure.sdk.service.impl;


import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeResponse;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.noinch.mall.biz.message.domain.service.AliyunSmsService;
import com.noinch.mall.biz.message.infrastructure.config.properties.AliyunSmsProperties;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunSmsServiceImpl implements AliyunSmsService {

    private final AliyunSmsProperties aliyunSmsProperties;
    private final Client client;

    @Override
    public Boolean sendSms(String phone) {

        String code = aliyunSmsProperties.getDefaultVerifyCode();

        String params = String.format("{\"code\":\"%s\",\"min\":\"%s\"}", code, aliyunSmsProperties.getExpireMinutes());
        SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = new SendSmsVerifyCodeRequest()
                .setSignName(aliyunSmsProperties.getSignName())
                .setTemplateCode(aliyunSmsProperties.getTemplateCode())
                .setTemplateParam(params)
                .setPhoneNumber(phone);

        try {
            SendSmsVerifyCodeResponse sendSmsVerifyCodeResponse = this.client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);

            if (sendSmsVerifyCodeResponse.getBody().getCode().equals("OK")) {
                return true;
            } else {
                throw new ServiceException(BaseErrorCode.REMOTE_SMS_SEND_ERROR.code());
            }

        } catch (Exception e) {
            log.error("send sms verify code failed, phone: {}", phone, e);
            throw new ServiceException(BaseErrorCode.REMOTE_SMS_VERIFYCODE_ERROR.code());
        }
    }

    @Override
    public Boolean verifySms(String phone, String verifyCode) {
        CheckSmsVerifyCodeRequest checkSmsVerifyCodeRequest = new CheckSmsVerifyCodeRequest()
                .setPhoneNumber(phone)
                .setVerifyCode(verifyCode);

        try {
            CheckSmsVerifyCodeResponse checkSmsVerifyCodeResponse = this.client.checkSmsVerifyCode(checkSmsVerifyCodeRequest);
            return checkSmsVerifyCodeResponse.getBody().getCode().equals("OK");

        } catch (Exception e) {
            log.error("verify sms verify code failed, phone: {}", phone, e);
            throw new ServiceException(BaseErrorCode.REMOTE_SMS_VERIFYCODE_ERROR.code());
        }
    }
}
