

package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 商品购物车添加适配请求对象
 *
 */
@Data
public class CartAddAdapterReqDTO {
    
    private String productId;
    
    private Integer productNum;
    
    private String userId;
}
