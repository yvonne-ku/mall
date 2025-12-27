

package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 收货地址新增适配请求对象
 *
 */
@Data
public class ReceiveAddressSaveAdapterReqDTO {
    
    private Boolean isDefault;
    
    private String streetName;
    
    private String tel;
    
    private String userId;
    
    private String userName;
}
