package com.noinch.mall.biz.product.domain.repository;

import com.noinch.mall.biz.product.domain.mode.ProductIndex;

import java.util.List;


public interface EsProductRepository {

    /**
     * 搜索商品
     */
    List<ProductIndex> searchProduct(String description, int pageNo, int pageSize);

    /**
     * 批量保存商品到ES
     */
    void saveBatch(List<ProductIndex> productIndexList);

    /**
     * 保存商品到ES
     */
    void save(ProductIndex productIndex);

    /**
     * 删除所有商品
     */
    void deleteAll();

    /**
     * 根据ID删除商品
     */
    void deleteById(String id);

}
