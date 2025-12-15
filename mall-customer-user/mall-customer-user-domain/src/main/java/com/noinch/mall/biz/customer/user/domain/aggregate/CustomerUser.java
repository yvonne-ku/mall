
package com.noinch.mall.biz.customer.user.domain.aggregate;

import com.noinch.mall.biz.customer.user.domain.toolkit.TokenUtil;
import lombok.*;
import com.noinch.mall.biz.customer.user.domain.dp.*;
import com.noinch.mall.biz.customer.user.domain.event.OperationLogEvent;
import com.noinch.mall.biz.customer.user.domain.mode.ReceiveAddress;

import java.util.List;

/**
 * C 端用户实体

 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class CustomerUser {
    
    private Long customerUserId;
    
    private CustomerUserName username;
    
    private CustomerUserPhone phone;
    
    private CustomerUserMail mail;
    
    private transient CustomerUserPassword password;
    
    private CustomerUserAccount account;
    
    private String receiver;
    
    private String verifyCode;
    
    private OperationLogEvent operationLogEvent;
    
    private List<ReceiveAddress> receiveAddresses;

    public String generateAccessToken() {
        return TokenUtil.genAccessToken(this.username.getUsername());
    }

    public Boolean checkPassword(String password) {
        return password.equals(this.password.getPassword());
    }

    public String getUsername() {
        return this.username.getUsername();
    }
    
    public String getAccount() {
        return this.account.getAccount();
    }
    
    public String getMail() {
        return this.mail.getMail();
    }
    
    public String getPhone() {
        return this.phone.getPhone();
    }
}
