package com.noinch.mall.biz.order.domain.service;

import com.noinch.mall.biz.order.domain.event.OrderCreateEvent;

public interface ProductStockRemoteService {

    void unlockProductStock(String orderSn);

    void lockProductStock(OrderCreateEvent event);
}
