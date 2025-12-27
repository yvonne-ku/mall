

package com.noinch.mall.biz.product.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品评论新增
 * */
@Data
public class ProductCommentSaveCommand {
    
    @Schema(description = "上级id，一级评论为0")
    private String parentId;
    
    @Schema(description = "商品id")
    private String productId;
    
    @Schema(description = "商品sku id")
    private String productSkuId;
    
    @Schema(description ="用户id")
    private String customerUserId;
    
    @Schema(description = "评分")
    private Integer star;
    
    @Schema(description = "评论")
    private String content;
    
    @Schema(description = "回复标识 0：用户 1：店家")
    private Integer commentFlag;
    
    @Schema(description = "匿名标识 0：匿名 1：不匿名")
    private Integer hideFlag;
    
    @Schema(description = "追加标识 0：否 1：是")
    private Integer appendFlag;
    
    @Schema(description = "评论图片/视频，json格式")
    private String resource;
}
