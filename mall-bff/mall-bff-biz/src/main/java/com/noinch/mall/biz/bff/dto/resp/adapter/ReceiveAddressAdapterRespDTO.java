

package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.Data;

/**
 * 收货地址适配返回对象
 *
 */
@Data
public class ReceiveAddressAdapterRespDTO {
    
    private String addressId;
    
    private Boolean isDefault;
    
    private String streetName;
    
    private String tel;
    
    private String userId;
    
    private String userName;
}
