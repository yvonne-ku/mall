

package com.noinch.mall.biz.product.domain.repository;

import com.noinch.mall.biz.product.domain.mode.ProductCategory;

/**
 * 商品分类仓储层
 */
public interface ProductCategoryRepository {
    
    /**
     * 查询所有商品分类信息
     *
     * @return
     */
    ProductCategory listAllProductCategory();
}
