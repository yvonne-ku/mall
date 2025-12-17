

package com.noinch.mall.biz.product.application.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.product.application.resp.ProductCategoryRespDTO;
import com.noinch.mall.biz.product.application.service.ProductCategoryService;
import com.noinch.mall.biz.product.domain.mode.ProductCategory;
import com.noinch.mall.biz.product.domain.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类
 * */
@Service
@AllArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    
    private final ProductCategoryRepository productCategoryRepository;
    
    @Override
    public List<ProductCategoryRespDTO> listAllProductCategory() {
        ProductCategory productCategory = productCategoryRepository.listAllProductCategory();
        return BeanUtil.copyToList(productCategory.getProductCategoryList(), ProductCategoryRespDTO.class);
    }
}
