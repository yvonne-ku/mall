

package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 商品购物车修改适配请求对象
 *
 */
@Data
public class CartUpdateAdapterReqDTO {
    
    private String productId;
    
    private Integer productNum;
    
    private String userId;
    
    private String checked;
}
