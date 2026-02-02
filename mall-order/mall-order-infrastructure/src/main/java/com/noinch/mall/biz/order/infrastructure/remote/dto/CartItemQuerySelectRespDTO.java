
package com.noinch.mall.biz.order.infrastructure.remote.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询选中购物车商品出参
 *
 */
@Data
public class CartItemQuerySelectRespDTO {
    
    @Schema(description = "商品 spu id")
    private String productId;
    
    @Schema(description = "商品 sku id")
    private String productSkuId;
    
    @Schema(description = "c 端用户 id")
    private String customerUserId;
    
    @Schema(description = "商品图")
    private String productPic;
    
    @Schema(description = "商品名称")
    private String productName;
    
    @Schema(description = "商品品牌")
    private String productBrand;
    
    @Schema(description = "商品价格")
    private BigDecimal productPrice;
    
    @Schema(description = "加购物车数量")
    private Integer productQuantity;
    
    @Schema(description = "商品规格，json 格式")
    private String productAttribute;
}
