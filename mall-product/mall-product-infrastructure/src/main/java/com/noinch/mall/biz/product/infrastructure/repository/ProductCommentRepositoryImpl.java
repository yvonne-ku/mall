

package com.noinch.mall.biz.product.infrastructure.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.product.domain.aggregate.ProductComment;
import com.noinch.mall.biz.product.domain.repository.ProductCommentRepository;
import com.noinch.mall.biz.product.infrastructure.dao.entity.ProductCommentDO;
import com.noinch.mall.biz.product.infrastructure.dao.mapper.ProductCommentMapper;
import com.noinch.mall.springboot.starter.common.enums.FlagEnum;
import org.springframework.stereotype.Repository;

/**
 * 商品评论仓储层
 */
@Repository
@RequiredArgsConstructor
public class ProductCommentRepositoryImpl implements ProductCommentRepository {
    
    private final ProductCommentMapper productCommentMapper;
    
    @Override
    public void saveProductComment(ProductComment productComment) {
        ProductCommentDO productCommentDO = new ProductCommentDO();
        BeanUtil.copyProperties(productComment, productCommentDO, CopyOptions.create().ignoreNullValue());
        productCommentMapper.insert(productCommentDO);
    }
    
    @Override
    public void removeProductComment(ProductComment productComment) {
        LambdaUpdateWrapper<ProductCommentDO> updateWrapper = Wrappers.lambdaUpdate(ProductCommentDO.class)
                .eq(ProductCommentDO::getProductId, productComment.getProductId())
                .eq(ProductCommentDO::getId, productComment.getId());
        productCommentMapper.delete(updateWrapper);
    }
    
    @Override
    public void appendProductComment(ProductComment productComment) {
        ProductCommentDO productCommentDO = new ProductCommentDO();
        BeanUtil.copyProperties(productComment, productCommentDO, CopyOptions.create().ignoreNullValue());
        productCommentDO.setAppendFlag(FlagEnum.TRUE.code());
        productCommentDO.setCommentFlag(FlagEnum.FALSE.code());
        productCommentMapper.insert(productCommentDO);
    }
}
