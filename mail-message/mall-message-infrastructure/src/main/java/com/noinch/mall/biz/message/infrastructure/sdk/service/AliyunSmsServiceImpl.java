package com.noinch.mall.biz.message.infrastructure.sdk.service;


import cn.hutool.core.date.DateTime;
import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeResponse;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.teaopenapi.models.Config;
import com.noinch.mall.biz.message.application.resp.PhoneSendRespDTO;
import com.noinch.mall.biz.message.application.service.AliyunSmsService;
import com.noinch.mall.biz.message.infrastructure.config.AliyunSmsConfig;
import com.noinch.mall.biz.message.infrastructure.dao.entity.AliyunSmsSendDO;
import com.noinch.mall.biz.message.infrastructure.dao.mapper.AliyunSmsSendMapper;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunSmsServiceImpl implements AliyunSmsService {

    private final AliyunSmsConfig aliyunSmsConfig;
    private final AliyunSmsSendMapper aliyunSmsSendMapper;
    private Client client;

    private void initClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(aliyunSmsConfig.getAccessKeyId())
                .setAccessKeySecret(aliyunSmsConfig.getAccessKeySecret())
                .setEndpoint(aliyunSmsConfig.getEndpoint());
        this.client = new Client(config);
    }

    @PostConstruct
    public void init() {
        try {
            initClient();
        } catch (Exception e) {
            log.error("init aliyun sms client failed", e);
        }
    }

    @Override
    public PhoneSendRespDTO sendSms(String phone) {

        String code = aliyunSmsConfig.getDefaultVerifyCode();
        //code = generateCode();

        String params = String.format("{\"code\":\"%s\",\"min\":\"%s\"}", code, aliyunSmsConfig.getExpireMinutes());

        AliyunSmsSendDO aliyunSmsSendDO = new AliyunSmsSendDO()
                .setPhoneNumber(phone)
                .setSendTime(new DateTime());

        SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = new SendSmsVerifyCodeRequest()
                .setSignName(aliyunSmsConfig.getSignName())
                .setTemplateCode(aliyunSmsConfig.getTemplateCode())
                .setTemplateParam(params)
                .setPhoneNumber(aliyunSmsSendDO.getPhoneNumber());

        try {
            SendSmsVerifyCodeResponse sendSmsVerifyCodeResponse = this.client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);

            if (! sendSmsVerifyCodeResponse.getBody().getCode().equals("OK")) {
                aliyunSmsSendDO.setStatus("FAILED");
                // 更新数据库
                aliyunSmsSendMapper.insert(aliyunSmsSendDO);
                throw new RuntimeException(sendSmsVerifyCodeResponse.getBody().getMessage());
            } else {
                aliyunSmsSendDO.setStatus("SUCCESS");
                // 更新数据库
                aliyunSmsSendMapper.insert(aliyunSmsSendDO);
                // 返回结果
                return new PhoneSendRespDTO()
                        .setPhone(phone)
                        .setStatus(aliyunSmsSendDO.getStatus().equals("SUCCESS"));
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

    private String generateCode() {
        int code = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }
}
