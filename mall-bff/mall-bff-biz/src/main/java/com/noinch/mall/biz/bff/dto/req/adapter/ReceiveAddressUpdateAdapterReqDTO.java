

package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 收货地址修改适配请求对象
 *
 */
@Data
public class ReceiveAddressUpdateAdapterReqDTO {
    
    private String addressId;
    
    private Boolean isDefault;
    
    private String streetName;
    
    private String tel;
    
    private String userId;
    
    private String userName;
}
