

package com.noinch.mall.biz.bff.remote.resp;

import lombok.Data;

/**
 * 用户注册响应
*
*/
@Data
public class UserRegisterRespDTO {

    /**
     * 用户 id
     */
    private Long customerUserId;

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;
}
