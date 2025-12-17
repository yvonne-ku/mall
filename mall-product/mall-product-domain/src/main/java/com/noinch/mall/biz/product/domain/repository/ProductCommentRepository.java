

package com.noinch.mall.biz.product.domain.repository;

import com.noinch.mall.biz.product.domain.aggregate.ProductComment;

/**
 * 商品评论仓储层
 */
public interface ProductCommentRepository {
    
    /**
     * 新增商品评论
     *
     * @param productComment 商品评论聚合根
     */
    void saveProductComment(ProductComment productComment);
    
    /**
     * 删除商品评论
     *
     * @param productComment 商品评论聚合根
     */
    void removeProductComment(ProductComment productComment);
    
    /**
     * 追加商品评论
     *
     * @param productComment 商品评论聚合根
     */
    void appendProductComment(ProductComment productComment);
}
