

package com.noinch.mall.biz.product.application.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品评论删除
 * */
@Data
public class ProductCommentRemoveCommand {
    
    @ApiModelProperty("id")
    private String id;
    
    @ApiModelProperty("商品id")
    private String productId;
}
