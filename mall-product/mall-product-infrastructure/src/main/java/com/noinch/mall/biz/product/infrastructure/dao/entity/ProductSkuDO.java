

package com.noinch.mall.biz.product.infrastructure.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;

import java.math.BigDecimal;

/**
 * 商品 SKU
 */
@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@TableName("product_sku")
public class ProductSkuDO extends BaseDO {
    
    /**
     * id
     */
    @NonNull
    private Long id;
    
    /**
     * 商品分类id
     */
    private Long categoryId;
    
    /**
     * 商品品牌id
     */
    private Long brandId;
    
    /**
     * 商品id
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
