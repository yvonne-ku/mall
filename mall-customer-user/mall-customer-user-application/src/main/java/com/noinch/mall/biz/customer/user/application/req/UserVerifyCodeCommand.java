
package com.noinch.mall.biz.customer.user.application.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户验证码
 * 1. 手机号验证码
 * 2. 邮箱验证码
*/
@Data
@ApiModel("用户验证码")
public class UserVerifyCodeCommand {

    @ApiModelProperty(value = "行为类型", notes = "register / login 等")
    private String type;

    @ApiModelProperty(value = "验证平台", notes = "phone / email 等")
    private String platform;

    @NotBlank(message = "接收者不能为空")
    @ApiModelProperty(value = "接收者", example = "m7798432@163.com")
    private String receiver;
}
