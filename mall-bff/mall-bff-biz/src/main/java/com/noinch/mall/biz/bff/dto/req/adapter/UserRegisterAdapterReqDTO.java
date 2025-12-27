package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

@Data
public class UserRegisterAdapterReqDTO {

    private String platform;

    private String username;

    private String password;

    private String phone;

    private String phoneValidCode;

    private String challenge;

    private String validate;

    private String seccode;

    private String statusKey;
}
