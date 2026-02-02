

package com.noinch.mall.biz.order.infrastructure.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证商品库存
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVerifyStockReqDTO {
    
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
