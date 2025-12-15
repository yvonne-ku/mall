package com.noinch.mall.biz.customer.user.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneVerifyReqDTO {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户输入的验证码
     */
    private String verifyCode;
}
