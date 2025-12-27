
package com.noinch.mall.biz.customer.user.application.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 用户登录返回实体
*
*/
@Data
@Builder
public class UserLoginRespDTO {
    
    @Schema(description = "用户ID")
    private Long customerUserId;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "账号")
    private String account;
    
    @Schema(description = "Token")
    private String accessToken;
}
