package com.noinch.mall.biz.bff.remote.req;

import lombok.Data;

/**
 * 用户注册
 *
 */
@Data
public class UserRegisterCommand {

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
