

package com.noinch.mall.biz.bff.remote.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改购物车商品SKU数量入参
 *
 */
@Data
public class CartItemNumUpdateReqDTO {
    
    @Schema(description = "商品id")
    private String productId;
    
    @Schema(description = "商品sku id")
    private String productSkuId;
    
    @Schema(description = "用户id")
    private String customerUserId;
    
    @Schema(description = "加购物车数量")
    private Integer productQuantity;
}
