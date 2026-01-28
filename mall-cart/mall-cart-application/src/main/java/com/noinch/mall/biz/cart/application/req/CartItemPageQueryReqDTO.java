
package com.noinch.mall.biz.cart.application.req;

import com.noinch.mall.springboot.starter.convention.page.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页查询购物车入参
 *
 */
@Data
public class CartItemPageQueryReqDTO extends PageRequest {
    
    @Schema(description = "c 端用户 id")
    private String customerUserId;
}
