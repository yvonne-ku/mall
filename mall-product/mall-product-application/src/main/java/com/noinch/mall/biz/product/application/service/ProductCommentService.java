

package com.noinch.mall.biz.product.application.service;

import com.noinch.mall.biz.product.application.req.ProductCommentAppendCommand;
import com.noinch.mall.biz.product.application.req.ProductCommentRemoveCommand;
import com.noinch.mall.biz.product.application.req.ProductCommentSaveCommand;

/**
 * 商品评论
 * */
public interface ProductCommentService {
    
    /**
     * 新增商品评论
     *
     * @param requestParam 新增商品评论入参
     */
    void saveProductComment(ProductCommentSaveCommand requestParam);
    
    /**
     * 删除商品评论
     *
     * @param requestParam 删除商品评论入参
     */
    void removeProductComment(ProductCommentRemoveCommand requestParam);
    
    /**
     * 追加商品评论
     *
     * @param requestParam 追加商品评论入参
     */
    void appendProductComment(ProductCommentAppendCommand requestParam);
}
