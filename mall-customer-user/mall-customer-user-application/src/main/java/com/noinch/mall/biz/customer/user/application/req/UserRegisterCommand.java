

package com.noinch.mall.biz.customer.user.application.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户注册
*
*/
@Data
@ApiModel("用户注册")
public class UserRegisterCommand {

    @ApiModelProperty(value = "验证平台", notes = "phone / email 等")
    @NotBlank(message = "验证平台不允许为空")
    private String platform;

    @ApiModelProperty(value = "用户名", example = "小马哥")
    @NotBlank(message = "用户名不允许为空")
    private String username;

    @ApiModelProperty(value = "密码", example = "xiao-ma-ge")
    @NotBlank(message = "密码不允许为空")
    private String password;

    @ApiModelProperty(value = "手机号", example = "15601166692")
    @NotBlank(message = "手机号不允许为空")
    private String phone;

    @ApiModelProperty(value = "手机号验证码", example = "123456")
    @NotBlank(message = "手机号验证码不允许为空")
    private String phoneValidCode;

    @ApiModelProperty(value = "GeeTest 验证 Challenge")
    private String challenge;

    @ApiModelProperty(value = "GeeTest 验证 Validate")
    private String validate;

    @ApiModelProperty(value = "GeeTest 验证 Seccode")
    private String seccode;

    @ApiModelProperty(value = "GeeTest 验证状态 Key")
    private String statusKey;
}
