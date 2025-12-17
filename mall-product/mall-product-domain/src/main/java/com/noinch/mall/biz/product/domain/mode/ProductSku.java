

package com.noinch.mall.biz.product.domain.mode;

import lombok.*;

import java.math.BigDecimal;

/**
 * 商品 SKU
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductSku {
    
    /**
     * id
     */
    @NonNull
    private Long id;
    
    /**
     * 商品 id
     */
    private Long productId;
    
    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 库存
     */
    private Integer stock;
    
    /**
     * 锁定库存
     */
    private Integer lockStock;
    
    /**
     * 图片
     */
    private String pic;
    
    /**
     * 属性，json 格式
     */
    private String attribute;
}
