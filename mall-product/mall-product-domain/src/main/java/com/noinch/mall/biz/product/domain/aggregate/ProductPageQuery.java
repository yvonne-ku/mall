

package com.noinch.mall.biz.product.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.noinch.mall.ddd.framework.core.domain.ValueObject;

/**
 * 商品分页查询
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageQuery implements ValueObject {
    
    private Integer sort;
    
    private Integer priceGt;
    
    private Integer priceLte;
    
    private Long current;
    
    private Long size;
}
