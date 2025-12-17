

package com.noinch.mall.biz.product.domain.mode;

import lombok.*;
import com.noinch.mall.biz.product.domain.dto.ProductCategoryDTO;

import java.util.List;

/**
 * 商品分类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class ProductCategory {
    
    /**
     * 商品分类信息
     */
    private List<ProductCategoryDTO> productCategoryList;
}
