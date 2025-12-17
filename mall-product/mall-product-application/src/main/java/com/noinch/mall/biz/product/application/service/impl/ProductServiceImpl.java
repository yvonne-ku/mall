

package com.noinch.mall.biz.product.application.service.impl;

import lombok.AllArgsConstructor;
import com.noinch.mall.biz.product.application.req.ProductLockStockCommand;
import com.noinch.mall.biz.product.application.req.ProductPageQuery;
import com.noinch.mall.biz.product.application.req.ProductStockVerifyQuery;
import com.noinch.mall.biz.product.application.req.ProductUnlockStockCommand;
import com.noinch.mall.biz.product.application.resp.ProductRespDTO;
import com.noinch.mall.biz.product.application.service.ProductService;
import com.noinch.mall.biz.product.domain.aggregate.Product;
import com.noinch.mall.biz.product.domain.aggregate.ProductStock;
import com.noinch.mall.biz.product.domain.aggregate.ProductStockDetail;
import com.noinch.mall.biz.product.domain.repository.ProductRepository;
import com.noinch.mall.springboot.starter.common.toolkit.BeanUtil;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务
 * */
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    public ProductRespDTO getProductBySpuId(Long spuId) {
        Product product = productRepository.getProductBySpuId(spuId);
        return BeanUtil.convert(product, ProductRespDTO.class);
    }
    
    @Override
    public Boolean verifyProductStock(List<ProductStockVerifyQuery> requestParams) {
        ProductStock productStock = ProductStock.builder().productStockDetails(BeanUtil.convert(requestParams, ProductStockDetail.class)).build();
        return productRepository.verifyProductStock(productStock);
    }
    
    @Override
    public Boolean lockProductStock(ProductLockStockCommand requestParam) {
        return productRepository.lockProductStock(BeanUtil.convert(requestParam, ProductStock.class));
    }
    
    @Override
    public Boolean unlockProductStock(ProductUnlockStockCommand requestParam) {
        return productRepository.unlockProductStock(BeanUtil.convert(requestParam, ProductStock.class));
    }
    
    @Override
    public PageResponse<ProductRespDTO> pageQueryProduct(ProductPageQuery requestParam) {
        Product product = Product.builder()
                .pageQuery(BeanUtil.convert(requestParam, com.noinch.mall.biz.product.domain.aggregate.ProductPageQuery.class))
                .build();
        PageResponse<Product> productPageResponse = productRepository.pageQueryProduct(product);
        return PageResponse.<ProductRespDTO>builder()
                .current(productPageResponse.getCurrent())
                .size(productPageResponse.getSize())
                .total(productPageResponse.getTotal())
                .records(BeanUtil.convert(productPageResponse.getRecords(), ProductRespDTO.class))
                .build();
    }
}
