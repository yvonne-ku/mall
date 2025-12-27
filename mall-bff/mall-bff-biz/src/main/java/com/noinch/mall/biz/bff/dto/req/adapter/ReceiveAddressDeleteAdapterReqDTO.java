

package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 收货地址删除适配请求对象
 *
 */
@Data
public class ReceiveAddressDeleteAdapterReqDTO {
    
    private String userId;
    
    private String addressId;
}
