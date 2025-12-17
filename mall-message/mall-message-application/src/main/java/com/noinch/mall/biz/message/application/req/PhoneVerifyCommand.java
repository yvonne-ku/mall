package com.noinch.mall.biz.message.application.req;

import lombok.Data;

@Data
public class PhoneVerifyCommand {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户输入的验证码
     */
    private String verifyCode;
}
