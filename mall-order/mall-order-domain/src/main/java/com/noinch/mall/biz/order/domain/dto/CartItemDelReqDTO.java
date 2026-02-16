

package com.noinch.mall.biz.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 删除购物车入参
 *
 */
@Data
public class CartItemDelReqDTO {
    
    @Schema(description = "用户ID")
    private String customerUserId;
    
    @Schema(description = "商品SKU集合")
    private List<String> productSkuIds;
}
