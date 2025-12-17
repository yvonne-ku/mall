

package com.noinch.mall.biz.product.application.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品库存验证
 * */
@Data
public class ProductStockVerifyQuery {
    
    @ApiModelProperty("商品 id")
    private String productId;
    
    @ApiModelProperty("商品 sku id")
    private String productSkuId;
    
    @ApiModelProperty("商品数量")
    private Integer productQuantity;
}
