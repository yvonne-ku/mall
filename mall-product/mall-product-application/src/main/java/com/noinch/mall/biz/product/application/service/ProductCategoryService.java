

package com.noinch.mall.biz.product.application.service;

import com.noinch.mall.biz.product.application.resp.ProductCategoryRespDTO;

import java.util.List;

/**
 * 商品分类
 * */
public interface ProductCategoryService {
    
    /**
     * 查询全部商品分类集合
     *
     * @return
     */
    List<ProductCategoryRespDTO> listAllProductCategory();
}
