package com.noinch.mall.biz.order.domain.service;

import com.noinch.mall.biz.order.domain.dto.ProductVerifyStockReqDTO;
import com.noinch.mall.biz.order.domain.event.OrderCreateEvent;

import java.util.List;

public interface ProductStockRemoteService {

    void unlockProductStock(String orderSn);

    void lockProductStock(OrderCreateEvent event);

    Boolean verifyProductStock(List<ProductVerifyStockReqDTO> requestParam);
}
