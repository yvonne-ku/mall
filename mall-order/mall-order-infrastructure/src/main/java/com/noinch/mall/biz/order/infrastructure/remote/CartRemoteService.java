

package com.noinch.mall.biz.order.infrastructure.remote;

import com.noinch.mall.biz.order.infrastructure.remote.dto.CartItemDelReqDTO;
import com.noinch.mall.biz.order.infrastructure.remote.dto.CartItemQuerySelectRespDTO;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 购物车远程调用
 *
 */
@FeignClient(value = "cart-service", url = "${mall.cart-service.url:}")
public interface CartRemoteService {
    
    /**
     * 根据用户ID查询选中状态购物车商品
     */
    @GetMapping("/api/cart/product/{customer_user_id}")
    Result<List<CartItemQuerySelectRespDTO>> querySelectCartByCustomerUserId(@PathVariable("customer_user_id") String customerUserId);
    
    /**
     * 删除购物车商品
     */
    @DeleteMapping("/api/cart/product")
    Result<Void> clearCartProduct(@RequestBody CartItemDelReqDTO requestParam);
}
