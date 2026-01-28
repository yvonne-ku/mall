
package com.noinch.mall.biz.cart.application.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.cart.application.req.*;
import com.noinch.mall.biz.cart.application.resp.CartItemQuerySelectRespDTO;
import com.noinch.mall.biz.cart.application.resp.CartItemRespDTO;
import com.noinch.mall.biz.cart.application.service.CartItemService;
import com.noinch.mall.biz.cart.domain.aggregate.CartItem;
import com.noinch.mall.biz.cart.domain.repository.CartItemRepository;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车
 *
 */
@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    
    private final CartItemRepository cartItemRepository;
    
    @Override
    public PageResponse<CartItemRespDTO> pageQueryCartItem(CartItemPageQueryReqDTO requestParam) {
        PageResponse<CartItem> cartItemPageResponse = cartItemRepository.pageQueryCartItem(requestParam.getCustomerUserId(), requestParam);
        return cartItemPageResponse.convert(each -> {
            CartItemRespDTO cartItemRespDTO = new CartItemRespDTO();
            BeanUtil.copyProperties(each, cartItemRespDTO);
            return cartItemRespDTO;
        });
    }
    
    @Override
    public List<CartItemQuerySelectRespDTO> querySelectCartItemByCustomerUserId(String customerUserId) {
        List<CartItem> cartItems = cartItemRepository.querySelectCartItemByCustomerUserId(customerUserId);
        return cartItems.stream().map(each -> {
            CartItemQuerySelectRespDTO cartItemQuerySelectRespDTO = new CartItemQuerySelectRespDTO();
            BeanUtil.copyProperties(each, cartItemQuerySelectRespDTO);
            return cartItemQuerySelectRespDTO;
        }).collect(Collectors.toList());
    }
    
    @Override
    public void addCartItem(CartItemAddReqDTO requestParam) {
        CartItem cartItem = CartItem.builder()
                .customerUserId(Long.parseLong(requestParam.getCustomerUserId()))
                .productName(requestParam.getProductName())
                .productId(Long.parseLong(requestParam.getProductId()))
                .productSkuId(Long.parseLong(requestParam.getProductSkuId()))
                .productBrand(requestParam.getProductBrand())
                .productAttribute(requestParam.getProductAttribute())
                .productPrice(requestParam.getProductPrice())
                .productQuantity(requestParam.getProductQuantity())
                .productPic(requestParam.getProductPic())
                .selectFlag(requestParam.getSelectFlag())
                .build();
        cartItemRepository.addCartItem(cartItem);
    }
    
    @Override
    public void updateCheckCartItem(CartItemCheckUpdateReqDTO requestParam) {
        CartItem cartItem = CartItem.builder()
                .selectFlag(requestParam.getSelectFlag())
                .customerUserId(Long.parseLong(requestParam.getCustomerUserId()))
                .productId(Long.parseLong(requestParam.getProductId()))
                .productSkuId(Long.parseLong(requestParam.getProductSkuId()))
                .build();
        cartItemRepository.updateCheckCartItem(cartItem);
    }
    
    @Override
    public void updateChecksCartItem(CartItemChecksUpdateReqDTO requestParam) {
        CartItem cartItem = CartItem.builder()
                .selectFlag(requestParam.getSelectFlag())
                .customerUserId(Long.parseLong(requestParam.getCustomerUserId()))
                .build();
        cartItemRepository.updateChecksCartItem(cartItem);
    }
    
    @Override
    public void updateQuantityCartItem(CartItemNumUpdateReqDTO requestParam) {
        CartItem cartItem = CartItem.builder()
                .productQuantity(requestParam.getProductQuantity())
                .customerUserId(Long.parseLong(requestParam.getCustomerUserId()))
                .productId(Long.parseLong(requestParam.getProductId()))
                .productSkuId(Long.parseLong(requestParam.getProductSkuId()))
                .build();
        cartItemRepository.updateCartItem(cartItem);
    }
    
    @Override
    public void clearCartItem(CartItemDelReqDTO requestParam) {
        CartItem cartItem = CartItem.builder()
                .customerUserId(Long.parseLong(requestParam.getCustomerUserId()))
                .productSkuIds(requestParam.getProductSkuIds())
                .build();
        cartItemRepository.deleteCartItem(cartItem);
    }
    
    @Override
    public int countCartItem(String customerUserId) {
        return cartItemRepository.countCartItem(customerUserId);
    }
    
    @Override
    public void clearCheckCartItem(CartItemDelCheckReqDTO requestParam) {
        CartItem cartItem = CartItem.builder()
                .customerUserId(Long.parseLong(requestParam.getCustomerUserId()))
                .build();
        cartItemRepository.deleteCheckCartItem(cartItem);
    }
}
