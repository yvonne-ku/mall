

package com.noinch.mall.biz.product.application.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.noinch.mall.biz.product.application.resp.ProductSkuRespDTO;
import com.noinch.mall.biz.product.application.resp.ProductSpuRespDTO;
import com.noinch.mall.biz.product.domain.mode.ProductIndex;
import com.noinch.mall.biz.product.domain.repository.EsProductRepository;
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
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务
 * */
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;

    private final EsProductRepository esProductRepository;
    
    @Override
    public ProductRespDTO getProductBySpuId(Long spuId) {
        Product product = productRepository.getProductBySpuId(spuId);
        ProductRespDTO productRespDTO = new ProductRespDTO();
        BeanUtil.copyProperties(product, productRespDTO);
        return productRespDTO;
    }
    
    @Override
    public Boolean verifyProductStock(List<ProductStockVerifyQuery> requestParams) {
        List<ProductStockDetail> productStockDetails = requestParams.stream().map(requestParam -> {
            ProductStockDetail productStockDetail = new ProductStockDetail();
            BeanUtil.copyProperties(requestParam, productStockDetail);
            return productStockDetail;
        }).collect(Collectors.toList());
        ProductStock productStock = ProductStock.builder()
                .productStockDetails(productStockDetails)
                .build();
        return productRepository.verifyProductStock(productStock);
    }
    
    @Override
    public Boolean lockProductStock(ProductLockStockCommand requestParam) {
        List<ProductStockDetail> productStockDetails = requestParam.getProductStockDetails().stream().map(requestParamDetail -> {
            ProductStockDetail productStockDetail = new ProductStockDetail();
            BeanUtil.copyProperties(requestParamDetail, productStockDetail);
            return productStockDetail;
        }).collect(Collectors.toList());
        return productRepository.lockProductStock(ProductStock.builder()
                .orderSn(requestParam.getOrderSn())
                .productStockDetails(productStockDetails)
                .build());
    }
    
    @Override
    public Boolean unlockProductStock(ProductUnlockStockCommand requestParam) {
        List<ProductStockDetail> productStockDetails = requestParam.getProductStockDetails().stream().map(requestParamDetail -> {
            ProductStockDetail productStockDetail = new ProductStockDetail();
            BeanUtil.copyProperties(requestParamDetail, productStockDetail);
            return productStockDetail;
        }).collect(Collectors.toList());
        return productRepository.unlockProductStock(ProductStock.builder()
                .orderSn(requestParam.getOrderSn())
                .productStockDetails(productStockDetails)
                .build());
    }
    
    @Override
    public PageResponse<ProductRespDTO> pageQueryProduct(ProductPageQuery requestParam) {
        com.noinch.mall.biz.product.domain.aggregate.ProductPageQuery productPageQuery = new com.noinch.mall.biz.product.domain.aggregate.ProductPageQuery();
        BeanUtil.copyProperties(requestParam, productPageQuery);
        Product product = Product.builder()
                .pageQuery(productPageQuery)
                .build();

        PageResponse<Product> productPageResponse = productRepository.pageQueryProduct(product);

        List<ProductRespDTO> productRespDTOS = productPageResponse.getRecords().stream().map(each -> {
            ProductRespDTO productRespDTO = new ProductRespDTO();
            BeanUtil.copyProperties(each, productRespDTO);
            return productRespDTO;
        }).collect(Collectors.toList());
        return PageResponse.<ProductRespDTO>builder()
                .current(productPageResponse.getCurrent())
                .size(productPageResponse.getSize())
                .total(productPageResponse.getTotal())
                .records(productRespDTOS)
                .build();
    }

    @Override
    public List<ProductRespDTO> searchProduct(String description, Integer page, Integer size) {
        List<ProductIndex> productIndices = esProductRepository.searchProduct(description, page, size);

        // ProductIndex 转化为 ProductRespDTO
        return productIndices.stream().map(each -> {
            ProductRespDTO productRespDTO = new ProductRespDTO();
            ProductSkuRespDTO productSkuRespDTO = new ProductSkuRespDTO();
            ProductSpuRespDTO productSpuRespDTO = new ProductSpuRespDTO();

            Product product = productRepository.getProductBySkuId(Long.valueOf(each.getId()));
            BeanUtil.copyProperties(product.getProductSkus().getFirst(), productSkuRespDTO);
            BeanUtil.copyProperties(product.getProductSpu(), productSpuRespDTO);

            productRespDTO.setProductSpu(productSpuRespDTO);
            productRespDTO.setProductSkus(Collections.singletonList(productSkuRespDTO));
            return productRespDTO;
        }).collect(Collectors.toList());
    }
}
