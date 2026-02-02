

package com.noinch.mall.biz.bff.dto.req.adapter;

import lombok.Data;

/**
 * 商品购物车删除适配请求对象
 *
 */
@Data
public class CartDeleteAdapterReqDTO {
    
    private String spuId;

    private String skuId;

    private String userId;
}
