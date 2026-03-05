package com.noinch.mall.biz.order.domain.service;

import com.noinch.mall.biz.order.domain.aggregate.Order;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.dto.ProductVerifyStockReqDTO;

import java.util.List;

public interface OrderInfraService {

    void delayCloseOrderMessageSend(Order order);

    void lockProductStock(Order order);

    void clearCartProduct(Order order);

    boolean delayCloseOrder(String orderSn);

    List<OrderProduct> querySelectCartItemByCustomerUserId(String customerUserId);

    Boolean verifyProductStock(List<ProductVerifyStockReqDTO> list);
}
