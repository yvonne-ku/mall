

package com.noinch.mall.biz.product.application.req;

import lombok.Data;
import com.noinch.mall.springboot.starter.convention.page.PageRequest;

/**
 * 商品分页查询
 * */
@Data
public class ProductPageQuery extends PageRequest {
    
    private Integer sort;
    
    private Integer priceGt;
    
    private Integer priceLte;
}
