
package com.noinch.mall.biz.customer.user.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户验证码
 * 1. 手机号验证码
 * 2. 邮箱验证码
*/
@Data
@Schema(description = "用户验证码")
public class UserVerifyCodeCommand {

    @Schema(description = "行为类型", example = "register / login 等")
    private String type;

    @Schema(description = "验证平台", example = "phone / email 等")
    private String platform;

    @NotBlank(message = "接收者不能为空")
    @Schema(description = "接收者", example = "m7798432@163.com")
    private String receiver;
}
