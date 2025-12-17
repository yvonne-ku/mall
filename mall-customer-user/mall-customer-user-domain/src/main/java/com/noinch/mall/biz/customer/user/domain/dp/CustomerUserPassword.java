

package com.noinch.mall.biz.customer.user.domain.dp;

import lombok.Data;

/**
 * C 端用户密码
 */
@Data
public class CustomerUserPassword {
    
    /**
     * 用户密码
     */
    private String password;
    
    public CustomerUserPassword(String password) {
        this.password = password;
    }
}
