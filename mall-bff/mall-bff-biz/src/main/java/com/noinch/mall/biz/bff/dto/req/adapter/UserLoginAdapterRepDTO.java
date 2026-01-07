
package com.noinch.mall.biz.bff.dto.req.adapter;

import io.swagger.v3.oas.annotations.media.Schema;
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

    private String captchaId;

    private String lotNumber;

    private String passToken;

    private String genTime;

    private String captchaOutput;
    
}
