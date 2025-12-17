

package com.noinch.mall.biz.product.domain.aggregate;

import lombok.*;
import com.noinch.mall.biz.product.domain.mode.ProductBrand;
import com.noinch.mall.biz.product.domain.mode.ProductSku;
import com.noinch.mall.biz.product.domain.mode.ProductSpu;
import com.noinch.mall.ddd.framework.core.domain.AggregateRoot;

import java.util.List;

/**
 * 商品信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class Product implements AggregateRoot {
    
    /**
     * 商品品牌
     */
    private ProductBrand productBrand;
    
    /**
     * 商品 SPU
     */
    private ProductSpu productSpu;
    
    /**
     * 商品 SKU 集合
     */
    private List<ProductSku> productSkus;
    
    /**
     * 分页查询
     */
    private ProductPageQuery pageQuery;
}
