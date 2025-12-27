
package com.noinch.mall.biz.customer.user.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户注册
 *
 */
@Data
@Schema(description = "用户注册")
public class UserRegisterCommand {

    @Schema(description = "验证平台", example = "phone / email 等")
    @NotBlank(message = "验证平台不允许为空")
    private String platform;

    @Schema(description = "用户名", example = "小马哥")
    @NotBlank(message = "用户名不允许为空")
    private String username;

    @Schema(description = "密码", example = "xiao-ma-ge")
    @NotBlank(message = "密码不允许为空")
    private String password;

    @Schema(description = "手机号", example = "15601166692")
    @NotBlank(message = "手机号不允许为空")
    private String phone;

    @Schema(description = "手机号验证码", example = "123456")
    @NotBlank(message = "手机号验证码不允许为空")
    private String phoneValidCode;

    @Schema(description = "GeeTest 验证 Challenge")
    private String challenge;

    @Schema(description = "GeeTest 验证 Validate")
    private String validate;

    @Schema(description = "GeeTest 验证 Seccode")
    private String seccode;

    @Schema(description = "GeeTest 验证状态 Key")
    private String statusKey;
}
