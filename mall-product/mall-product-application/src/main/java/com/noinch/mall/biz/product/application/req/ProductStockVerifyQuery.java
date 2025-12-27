

package com.noinch.mall.biz.product.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品库存验证
 * */
@Data
public class ProductStockVerifyQuery {

    @Schema(description = "商品 id")
    private String productId;
    
    @Schema(description = "商品 sku id")
    private String productSkuId;
    
    @Schema(description = "商品数量")
    private Integer productQuantity;
}
