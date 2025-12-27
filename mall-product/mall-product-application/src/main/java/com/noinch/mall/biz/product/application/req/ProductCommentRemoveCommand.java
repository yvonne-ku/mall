

package com.noinch.mall.biz.product.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品评论删除
 * */
@Data
public class ProductCommentRemoveCommand {
    
    @Schema(description = "id")
    private String id;
    
    @Schema(description = "商品id")
    private String productId;
}
