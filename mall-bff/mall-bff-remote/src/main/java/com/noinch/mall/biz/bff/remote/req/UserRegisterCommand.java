package com.noinch.mall.biz.bff.remote.req;

import io.swagger.v3.oas.annotations.media.Schema;
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

    private String captchaId;

    private String lotNumber;

    private String passToken;

    private String genTime;

    private String captchaOutput;
}
