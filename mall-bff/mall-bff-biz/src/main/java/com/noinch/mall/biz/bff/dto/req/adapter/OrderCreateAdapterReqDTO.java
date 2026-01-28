
package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 订单创建适配请求对象
 *
 */
@Data
public class OrderCreateAdapterReqDTO {
    
    private Integer orderTotal;
    
    private String streetName;
    
    private String tel;
    
    private String userId;
    
    private String userName;
}
