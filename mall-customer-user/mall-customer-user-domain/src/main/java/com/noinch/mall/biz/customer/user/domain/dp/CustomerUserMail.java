

package com.noinch.mall.biz.customer.user.domain.dp;

import lombok.Data;

/**
 * C 端用户邮箱
 */
@Data
public class CustomerUserMail {
    
    /**
     * 邮箱
     */
    private final String mail;
    
    public CustomerUserMail(String mail) {
        this.mail = mail;
    }
}
