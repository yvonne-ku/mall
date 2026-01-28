
package com.noinch.mall.biz.cart.application.service;

import com.noinch.mall.biz.cart.application.req.*;
import com.noinch.mall.biz.cart.application.resp.CartItemQuerySelectRespDTO;
import com.noinch.mall.biz.cart.application.resp.CartItemRespDTO;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;

import java.util.List;

/**
 * 购物车
 *
 */
public interface CartItemService {
    
    /**
     * 分页查询购物车商品
     *
     * @param requestParam 请求入参
     * @return 购物车商品分页返回数据
     */
    PageResponse<CartItemRespDTO> pageQueryCartItem(CartItemPageQueryReqDTO requestParam);
    
    /**
     * 查询用户选中购物车商品
     *
     * @param customerUserId 用户 ID
     * @return
     */
    List<CartItemQuerySelectRespDTO> querySelectCartItemByCustomerUserId(String customerUserId);
    
    /**
     * 新增商品到购物车
     *
     * @param requestParam 请求入参
     */
    void addCartItem(CartItemAddReqDTO requestParam);
    
    /**
     * 修改购物车商品勾选状态
     *
     * @param requestParam 请求入参
     */
    void updateCheckCartItem(CartItemCheckUpdateReqDTO requestParam);
    
    /**
     * 修改全部购物车商品勾选状态
     *
     * @param requestParam 请求入参
     */
    void updateChecksCartItem(CartItemChecksUpdateReqDTO requestParam);
    
    /**
     * 修改购物车商品
     *
     * @param requestParam 请求入参
     */
    void updateQuantityCartItem(CartItemNumUpdateReqDTO requestParam);
    
    /**
     * 清理购物车商品
     *
     * @param requestParam 请求入参
     */
    void clearCartItem(CartItemDelReqDTO requestParam);
    
    /**
     * 统计用户购物车商品数量
     *
     * @param customerUserId 用户 ID
     * @return 统计购物车商品数量
     */
    int countCartItem(String customerUserId);
    
    /**
     * 删除选中购物车商品
     *
     * @param requestParam 请求入参
     */
    void clearCheckCartItem(CartItemDelCheckReqDTO requestParam);
}
