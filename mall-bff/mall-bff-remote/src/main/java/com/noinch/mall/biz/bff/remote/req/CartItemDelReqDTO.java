

package com.noinch.mall.biz.bff.remote.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 删除购物车入参
 *
 */
@Data
public class CartItemDelReqDTO {
    
    @Schema(description = "用户id")
    private String customerUserId;
    
    @Schema(description = "商品 sku id 集合")
    private List<String> productSkuIds;
}
