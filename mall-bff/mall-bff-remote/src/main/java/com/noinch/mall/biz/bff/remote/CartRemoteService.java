
package com.noinch.mall.biz.bff.remote;

import com.noinch.mall.biz.bff.remote.req.CartItemAddReqDTO;
import com.noinch.mall.biz.bff.remote.req.CartItemCheckUpdateReqDTO;
import com.noinch.mall.biz.bff.remote.req.CartItemChecksUpdateReqDTO;
import com.noinch.mall.biz.bff.remote.req.CartItemDelCheckReqDTO;
import com.noinch.mall.biz.bff.remote.req.CartItemDelReqDTO;
import com.noinch.mall.biz.bff.remote.req.CartItemNumUpdateReqDTO;
import com.noinch.mall.biz.bff.remote.resp.CartItemQuerySelectRespDTO;
import com.noinch.mall.biz.bff.remote.resp.CartItemRespDTO;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品购物车远程调用服务
 *
 */
@FeignClient(value = "cart-service", url = "${mall.aggregation-url.cart-service}")
public interface CartRemoteService {

    /**
     * 分页查询购物车商品
     */
    @GetMapping("/api/cart/product/page")
    Result<PageResponse<CartItemRespDTO>> pageQueryCartItem(@RequestParam("customerUserId") String customerUserId,
                                                            @RequestParam("current") Long current,
                                                            @RequestParam("size") Long size
    );

    /**
     * 查询用户选中购物车商品
     */
    @GetMapping("/api/cart/product/{customerUserId}")
    Result<List<CartItemQuerySelectRespDTO>> querySelectCartItemByCustomerUserId(@PathVariable("customerUserId") String customerUserId);

    /**
     * 新增商品到购物车
     */
    @PostMapping("/api/cart/product")
    Result<Void> addCartItem(@RequestBody CartItemAddReqDTO requestParam);

    /**
     * 修改购物车商品勾选状态
     */
    @PutMapping("/api/cart/product/check")
    Result<Void> updateCheckCartItem(@RequestBody CartItemCheckUpdateReqDTO requestParam);

    /**
     * 修改全部购物车商品勾选状态
     */
    @PutMapping("/api/cart/product/checks")
    Result<Void> updateChecksCartItem(@RequestBody CartItemChecksUpdateReqDTO requestParam);

    /**
     * 修改购物车商品SKU数量
     */
    @PutMapping("/api/cart/product/num")
    Result<Void> updateQuantityCartItem(@RequestBody CartItemNumUpdateReqDTO requestParam);

    /**
     * 删除购物车商品
     */
    @DeleteMapping("/api/cart/product")
    Result<Void> clearCartItem(@RequestBody CartItemDelReqDTO requestParam);

    /**
     * 删除选中购物车商品
     */
    @DeleteMapping("/api/cart/product/delete/checks")
    Result<Void> clearCheckCartItem(@RequestBody CartItemDelCheckReqDTO requestParam);
}
