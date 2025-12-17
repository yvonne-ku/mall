

package com.noinch.mall.biz.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品SKU库存
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockDetailDTO {
    
    /**
     * 商品 SPU ID
     */
    private String productId;
    
    /**
     * 商品 SKU ID
     */
    private String productSkuId;
    
    /**
     * 商品数量
     */
    private Integer productQuantity;
}
