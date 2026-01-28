package com.noinch.mall.biz.cart.interfaces.controller;

import com.noinch.mall.biz.cart.application.req.*;
import com.noinch.mall.biz.cart.application.resp.CartItemQuerySelectRespDTO;
import com.noinch.mall.biz.cart.application.resp.CartItemRespDTO;
import com.noinch.mall.biz.cart.application.service.CartItemService;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.web.Results;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 购物车控制器
 *
 */
@RestController
@AllArgsConstructor
@Tag(name = "购物车")
@RequestMapping("/api/cart/product")
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/page")
    @Operation(description = "分页查询购物车商品")
    public Result<PageResponse<CartItemRespDTO>> pageQueryCartItem(CartItemPageQueryReqDTO requestParam) {
        PageResponse<CartItemRespDTO> resultPage = cartItemService.pageQueryCartItem(requestParam);
        return Results.success(resultPage);
    }

    @GetMapping("/{customerUserId}")
    @Operation(description = "查询用户选中购物车商品")
    public Result<List<CartItemQuerySelectRespDTO>> querySelectCartItemByCustomerUserId(@PathVariable("customerUserId") String customerUserId) {
        List<CartItemQuerySelectRespDTO> result = cartItemService.querySelectCartItemByCustomerUserId(customerUserId);
        return Results.success(result);
    }

    @PostMapping
    @Operation(description = "新增商品到购物车")
    public Result<Void> addCartItem(@RequestBody CartItemAddReqDTO requestParam) {
        cartItemService.addCartItem(requestParam);
        return Results.success();
    }

    @PutMapping("/check")
    @Operation(description = "修改购物车商品勾选状态")
    public Result<Void> updateCheckCartItem(@RequestBody CartItemCheckUpdateReqDTO requestParam) {
        cartItemService.updateCheckCartItem(requestParam);
        return Results.success();
    }

    @PutMapping("/checks")
    @Operation(description = "修改全部购物车商品勾选状态")
    public Result<Void> updateChecksCartItem(@RequestBody CartItemChecksUpdateReqDTO requestParam) {
        cartItemService.updateChecksCartItem(requestParam);
        return Results.success();
    }

    @PutMapping("/num")
    @Operation(description = "修改购物车商品SKU数量")
    public Result<Void> updateNumCartItem(@RequestBody CartItemNumUpdateReqDTO requestParam) {
        cartItemService.updateQuantityCartItem(requestParam);
        return Results.success();
    }

    @DeleteMapping
    @Operation(description = "删除购物车商品")
    public Result<Void> clearCartProduct(@RequestBody CartItemDelReqDTO requestParam) {
        cartItemService.clearCartItem(requestParam);
        return Results.success();
    }

    @DeleteMapping("/delete/checks")
    @Operation(description = "删除选中购物车商品")
    public Result<Void> clearCheckCartProduct(@RequestBody CartItemDelCheckReqDTO requestParam) {
        cartItemService.clearCheckCartItem(requestParam);
        return Results.success();
    }

    @GetMapping("/count/{customerUserId}")
    @Operation(description = "统计用户购物车商品数量")
    public Result<Integer> countUserCartItem(@PathVariable("customerUserId") String customerUserId) {
        int result = cartItemService.countCartItem(customerUserId);
        return Results.success(result);
    }
}
