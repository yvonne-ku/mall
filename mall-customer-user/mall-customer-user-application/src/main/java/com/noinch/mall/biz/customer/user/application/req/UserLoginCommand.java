
package com.noinch.mall.biz.customer.user.application.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

/**
 * 用户登录
*
*/
@Data
@ApiModel("用户登录")
public class UserLoginCommand {
    
    @ApiModelProperty(value = "登录方式", notes = "username / mail / phone / wechat")
    private String type;

    @ApiModelProperty(value = "用户名", example = "John")
    private String username;

    @ApiModelProperty(value = "密码", example = "xiao-ma-ge")
    private String password;

    @ApiModelProperty(value = "账号", example = "15601166692")
    private String account;
    
    @ApiModelProperty(value = "手机号", example = "15601166692")
    private String phone;
    
    @ApiModelProperty(value = "邮箱", example = "m7798432@163.com", notes = "实际发送时更改为自己邮箱")
    @Email
    private String mail;
    
    @ApiModelProperty(value = "邮箱验证码", example = "123456")
    private String mailValidCode;

    @ApiModelProperty(value = "GeeTest 验证 Challenge")
    private String challenge;

    @ApiModelProperty(value = "GeeTest 验证 Validate")
    private String validate;

    @ApiModelProperty(value = "GeeTest 验证 Seccode")
    private String seccode;

    @ApiModelProperty(value = "GeeTest 验证状态 Key")
    private String statusKey;

}
