
package com.noinch.mall.biz.bff.remote.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

/**
 * 用户登录
*
*/
@Data
public class UserLoginCommand {
    
    private String type;

    private String username;

    private String password;

    private String account;
    
    private String phone;
    
    private String mail;
    
    private String mailValidCode;

    private String captchaId;

    private String lotNumber;

    private String passToken;

    private String genTime;

    private String captchaOutput;

}
