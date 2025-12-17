

package com.noinch.mall.biz.product.infrastructure.repository;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.product.domain.dto.ProductCategoryDTO;
import com.noinch.mall.biz.product.domain.mode.ProductCategory;
import com.noinch.mall.biz.product.domain.repository.ProductCategoryRepository;
import com.noinch.mall.biz.product.infrastructure.dao.entity.ProductCategoryDO;
import com.noinch.mall.biz.product.infrastructure.dao.mapper.ProductCategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品分类仓储层
 */
@Component
@AllArgsConstructor
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {
    
    private final ProductCategoryMapper productCategoryMapper;
    
    @Override
    public ProductCategory listAllProductCategory() {
        LambdaQueryWrapper<ProductCategoryDO> queryWrapper = Wrappers.lambdaQuery(ProductCategoryDO.class).eq(ProductCategoryDO::getStatus, 0);
        List<ProductCategoryDO> productCategoryDOS = productCategoryMapper.selectList(queryWrapper);
        return ProductCategory.builder().productCategoryList(BeanUtil.copyToList(productCategoryDOS, ProductCategoryDTO.class)).build();
    }
}
