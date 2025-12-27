
package com.noinch.mall.biz.bff.remote.req;

import lombok.Data;

/**
 * 用户验证码
 * 1. 手机号验证码
 * 2. 邮箱验证码
*/
@Data
public class UserVerifyCodeCommand {

    private String type;

    private String platform;

    private String receiver;
}
