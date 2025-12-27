

package com.noinch.mall.biz.customer.user.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 删除收货地址
*
*/
@Data
@Schema(description = "删除收货地址")
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
