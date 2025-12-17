

package com.noinch.mall.biz.customer.user.domain.dp;

import lombok.Data;

/**
 * C 端用户账号
 */
@Data
public class CustomerUserAccount {
    
    /**
     * 账号
     */
    private String account;
    
    public CustomerUserAccount(String account) {
        this.account = account;
    }
}
