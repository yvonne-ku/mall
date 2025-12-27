
package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 用户登录适配请求对象
 *
 */
@Data
public class UserLoginAdapterRepDTO {

    private String type;

    private String username;

    private String password;

    private String challenge;
    
    private String seccode;
    
    private String statusKey;

    private String validate;
    
}
