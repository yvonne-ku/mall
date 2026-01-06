

package com.noinch.mall.biz.product.application.service;

import com.noinch.mall.biz.product.application.req.ProductLockStockCommand;
import com.noinch.mall.biz.product.application.req.ProductPageQuery;
import com.noinch.mall.biz.product.application.req.ProductStockVerifyQuery;
import com.noinch.mall.biz.product.application.req.ProductUnlockStockCommand;
import com.noinch.mall.biz.product.application.resp.ProductRespDTO;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;

import java.util.List;

/**
 * 商品服务
 * */
public interface ProductService {
    
    /**
     * 根据 spuId 查询商品信息
     *
     * @param spuId 商品 SKU ID
     * @return 商品详细信息
     */
    ProductRespDTO getProductBySpuId(Long spuId);
    
    /**
     * 验证商品库存
     *
     * @param requestParams 商品 SKU ID 以及数量集合
     * @return 商品库存是否充足，全部商品库存验证无问题返回 {@link Boolean#TRUE}，反之返回 {@link Boolean#FALSE}
     */
    Boolean verifyProductStock(List<ProductStockVerifyQuery> requestParams);
    
    /**
     * 锁定商品库存
     *
     * @param requestParam 商品锁定库存入参
     * @return 是否锁定相关商品库存返回结果，锁定成功返回 {@link Boolean#TRUE}，反之返回 {@link Boolean#FALSE}
     */
    Boolean lockProductStock(ProductLockStockCommand requestParam);
    
    /**
     * 解锁商品库存
     *
     * @param requestParam 商品解锁库存入参
     * @return 是否解锁相关商品库存返回结果，解锁成功返回 {@link Boolean#TRUE}，反之返回 {@link Boolean#FALSE}
     */
    Boolean unlockProductStock(ProductUnlockStockCommand requestParam);
    
    /**
     * 分页查询商品集合
     *
     * @param requestParam 分页查询参数
     * @return 分页查询商品集合返回数据
     */
    PageResponse<ProductRespDTO> pageQueryProduct(ProductPageQuery requestParam);

     /**
     * 搜索商品
     *
     * @param description 搜索描述
     * @return 搜索商品返回数据
     */
     List<ProductRespDTO> searchProduct(String description, Integer page, Integer size, Integer sort, Integer priceGt, Integer priceLte);
}
