

package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 商品购物车全选编辑适配请求对象
 *
 */
@Data
public class CartChecksAdapterReqDTO {
    
    private Boolean checked;
    
    private String userId;
}
