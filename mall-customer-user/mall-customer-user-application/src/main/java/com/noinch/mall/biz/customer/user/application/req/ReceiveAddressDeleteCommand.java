

package com.noinch.mall.biz.customer.user.application.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 删除收货地址
*
*/
@Data
@ApiModel("删除收货地址")
public class ReceiveAddressDeleteCommand {
    
    /**
     * 收货地址id
     */
    private String id;
    
    /**
     * c端用户id
     */
    private String customerUserId;
}
