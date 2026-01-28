
package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.Data;

/**
 * 订单地址适配返回对象
 *
 */
@Data
public class OrderAddressAdapterRespDTO {
    
    private String addressId;
    
    private Integer isDefault;
    
    private String streetName;
    
    private String tel;
    
    private String userId;
    
    private String userName;
}
