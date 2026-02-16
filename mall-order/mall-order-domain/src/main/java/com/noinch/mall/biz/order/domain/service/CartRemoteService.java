package com.noinch.mall.biz.order.domain.service;

import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.dto.CartItemDelReqDTO;

import java.util.List;

public interface CartRemoteService {

    List<OrderProduct> querySelectCartItemByCustomerUserId(String customerUserId);

    void clearCartProduct(CartItemDelReqDTO cartItemDelReqDTO);
}
