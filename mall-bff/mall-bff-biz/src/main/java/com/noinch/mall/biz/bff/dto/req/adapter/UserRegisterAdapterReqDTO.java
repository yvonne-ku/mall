package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

@Data
public class UserRegisterAdapterReqDTO {

    private String platform;

    private String username;

    private String password;

    private String phone;

    private String phoneValidCode;

    private String captchaId;

    private String lotNumber;

    private String passToken;

    private String genTime;

    private String captchaOutput;
}
