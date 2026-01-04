package com.noinch.mall.biz.product.domain.repository;

import com.noinch.mall.biz.product.domain.mode.ProductIndex;

import java.util.List;


public interface EsProductRepository {

    /**
     * 搜索商品
     */
    List<ProductIndex> searchProduct(String description, int pageNo, int pageSize);

    /**
     * 保存商品到ES
     */
    void save(ProductIndex productIndex);

    /**
     * 根据ID删除商品
     */
    void deleteById(String id);

    /**
     * 根据ID查询商品
     */
    ProductIndex findById(String id);
}
