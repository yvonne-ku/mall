
package com.noinch.mall.biz.customer.user.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

/**
 * 用户登录
*
*/
@Data
@Schema(description = "用户登录")
public class UserLoginCommand {
    
    @Schema(description = "登录方式", example = "username / mail / phone / wechat")
    private String type;

    @Schema(description = "用户名", example = "John")
    private String username;

    @Schema(description = "密码", example = "xiao-ma-ge")
    private String password;

    @Schema(description = "账号", example = "15601166692")
    private String account;
    
    @Schema(description = "手机号", example = "15601166692")
    private String phone;
    
    @Schema(description = "邮箱", example = "m7798432@163.com")
    @Email
    private String mail;
    
    @Schema(description = "邮箱验证码", example = "123456")
    private String mailValidCode;

    @Schema(description = "GeeTest 凭证 captchaId")
    private String captchaId;

    @Schema(description = "GeeTest 凭证 流水号")
    private String lotNumber;

    @Schema(description = "GeeTest 凭证 通过凭据")
    private String passToken;

    @Schema(description = "GeeTest 凭证 时间戳")
    private String genTime;

    @Schema(description = "GeeTest 凭证 加密信息")
    private String captchaOutput;
}
