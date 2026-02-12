package com.noinch.mall.biz.order.domain.service.impl;

import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import com.noinch.mall.biz.order.domain.service.OrderDomainService;
import com.noinch.mall.biz.order.domain.service.ProductStockRemoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class OrderDomainServiceImpl implements OrderDomainService {

    private final OrderRepository orderRepository;
    private final ProductStockRemoteService productStockRemoteService;

    @Override
    public boolean delayCloseOrder(String orderSn) {
        orderRepository.closeOrder(orderSn);                // 1. 关闭订单
        productStockRemoteService.unlockProductStock(orderSn);   // 2. 回滚库存
        return true;
    }
}
