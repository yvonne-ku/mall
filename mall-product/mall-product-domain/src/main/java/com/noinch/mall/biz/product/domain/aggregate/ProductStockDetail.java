

package com.noinch.mall.biz.product.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.noinch.mall.ddd.framework.core.domain.ValueObject;

/**
 * 商品库存详情
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockDetail implements ValueObject {
    
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
