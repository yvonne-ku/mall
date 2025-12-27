

package com.noinch.mall.biz.bff.remote.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录返回实体
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRespDTO {
    
    @Schema(description = "用户ID")
    private Long customerUserId;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "账号")
    private String accountNumber;
    
    @Schema(description = "Token")
    private String accessToken;
}
