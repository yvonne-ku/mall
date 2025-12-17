

package com.noinch.mall.biz.product.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.product.domain.aggregate.ProductComment;
import com.noinch.mall.biz.product.domain.repository.ProductCommentRepository;
import com.noinch.mall.biz.product.infrastructure.dao.entity.ProductCommentDO;
import com.noinch.mall.biz.product.infrastructure.dao.mapper.ProductCommentMapper;
import com.noinch.mall.springboot.starter.common.enums.FlagEnum;
import com.noinch.mall.springboot.starter.common.toolkit.BeanUtil;
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
        ProductCommentDO productCommentDO = BeanUtil.convert(productComment, ProductCommentDO.class);
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
        ProductCommentDO productCommentDO = BeanUtil.convert(productComment, ProductCommentDO.class);
        productCommentDO.setAppendFlag(FlagEnum.TRUE.code());
        productCommentDO.setCommentFlag(FlagEnum.FALSE.code());
        productCommentMapper.insert(productCommentDO);
    }
}
