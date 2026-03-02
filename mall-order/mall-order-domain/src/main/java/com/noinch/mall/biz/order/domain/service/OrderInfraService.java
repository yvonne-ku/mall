package com.noinch.mall.biz.order.domain.service;

import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.dto.CartItemDelReqDTO;
import com.noinch.mall.biz.order.domain.dto.ProductVerifyStockReqDTO;
import com.noinch.mall.biz.order.domain.event.DelayCloseOrderEvent;
import com.noinch.mall.biz.order.domain.event.OrderCreateEvent;

import java.util.List;

public interface OrderInfraService {

    void delayCloseOrderMessageSend(DelayCloseOrderEvent event);

    void lockProductStock(OrderCreateEvent event);

    void clearCartProduct(CartItemDelReqDTO cartItemDelReqDTO);

    boolean delayCloseOrder(String orderSn);

    List<OrderProduct> querySelectCartItemByCustomerUserId(String customerUserId);

    Boolean verifyProductStock(List<ProductVerifyStockReqDTO> list);
}
