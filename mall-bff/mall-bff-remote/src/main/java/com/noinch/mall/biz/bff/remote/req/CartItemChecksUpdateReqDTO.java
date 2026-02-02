

package com.noinch.mall.biz.bff.remote.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改购物车商品勾选状态入参
 *
 */
@Data
public class CartItemChecksUpdateReqDTO {
    
    @Schema(description = "用户id")
    private String customerUserId;
    
    @Schema(description = "选中标志")
    private Integer selectFlag;
}
