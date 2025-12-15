
package com.noinch.mall.biz.customer.user.application.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录返回实体
*
*/
@Data
@Builder
public class UserLoginRespDTO {
    
    @ApiModelProperty(value = "用户ID")
    private Long customerUserId;
    
    @ApiModelProperty(value = "用户名")
    private String username;
    
    @ApiModelProperty(value = "账号")
    private String account;
    
    @ApiModelProperty(value = "Token")
    private String accessToken;
}
