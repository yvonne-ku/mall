

package com.noinch.mall.biz.customer.user.domain.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.noinch.mall.biz.customer.user.domain.dto.CustomerUserDTO;
import com.noinch.mall.ddd.framework.core.domain.DomainEvent;

/**
 * C 端用户操作日志事件
 */
@Data
@NoArgsConstructor
public class OperationLogEvent implements DomainEvent {
    
    /**
     * 之前的
     */
    private CustomerUserDTO beforeCustomerUser;
    
    /**
     * 操作后
     */
    private CustomerUserDTO afterCustomerUser;
    
    public OperationLogEvent(CustomerUserDTO afterCustomerUser) {
        this.afterCustomerUser = afterCustomerUser;
    }
    
    public OperationLogEvent(CustomerUserDTO beforeCustomerUser, CustomerUserDTO afterCustomerUser) {
        this.beforeCustomerUser = beforeCustomerUser;
        this.afterCustomerUser = afterCustomerUser;
    }
}
