
package com.noinch.mall.biz.bff.remote.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 删除选中购物车商品入参
 *
 */
@Data
public class CartItemDelCheckReqDTO {
    
    @Schema(description = "用户id")
    private String customerUserId;
}
