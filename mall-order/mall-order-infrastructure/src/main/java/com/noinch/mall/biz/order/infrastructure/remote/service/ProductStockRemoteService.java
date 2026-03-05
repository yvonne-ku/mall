package com.noinch.mall.biz.order.infrastructure.remote.service;

import com.noinch.mall.biz.order.domain.aggregate.Order;
import com.noinch.mall.biz.order.domain.dto.ProductVerifyStockReqDTO;

import java.util.List;

public interface ProductStockRemoteService {

    void unlockProductStock(String orderSn);

    void lockProductStock(Order order);

    Boolean verifyProductStock(List<ProductVerifyStockReqDTO> requestParam);
}
