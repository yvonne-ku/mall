

package com.noinch.mall.biz.product.application.service.impl;

import com.noinch.mall.springboot.starter.common.toolkit.BeanUtil;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.product.application.req.ProductCommentAppendCommand;
import com.noinch.mall.biz.product.application.req.ProductCommentRemoveCommand;
import com.noinch.mall.biz.product.application.req.ProductCommentSaveCommand;
import com.noinch.mall.biz.product.application.service.ProductCommentService;
import com.noinch.mall.biz.product.domain.aggregate.ProductComment;
import com.noinch.mall.biz.product.domain.repository.ProductCommentRepository;
import org.springframework.stereotype.Service;

/**
 * 商品评论
 * */
@Service
@RequiredArgsConstructor
public class ProductCommentServiceImpl implements ProductCommentService {
    
    private final ProductCommentRepository productCommentRepository;
    
    @Override
    public void saveProductComment(ProductCommentSaveCommand requestParam) {
        ProductComment productComment = BeanUtil.convert(requestParam, ProductComment.class);
        productCommentRepository.saveProductComment(productComment);
    }
    
    @Override
    public void removeProductComment(ProductCommentRemoveCommand requestParam) {
        ProductComment productComment = BeanUtil.convert(requestParam, ProductComment.class);
        productCommentRepository.removeProductComment(productComment);
    }
    
    @Override
    public void appendProductComment(ProductCommentAppendCommand requestParam) {
        ProductComment productComment = BeanUtil.convert(requestParam, ProductComment.class);
        productCommentRepository.appendProductComment(productComment);
    }
}
