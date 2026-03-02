package com.noinch.mall.biz.order.infrastructure.service;

import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.dto.CartItemDelReqDTO;
import com.noinch.mall.biz.order.domain.dto.ProductVerifyStockReqDTO;
import com.noinch.mall.biz.order.domain.event.DelayCloseOrderEvent;
import com.noinch.mall.biz.order.domain.event.OrderCreateEvent;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import com.noinch.mall.biz.order.domain.service.OrderInfraService;
import com.noinch.mall.biz.order.infrastructure.mq.producer.DelayCloseOrderProducer;
import com.noinch.mall.biz.order.infrastructure.remote.CartRemoteClient;
import com.noinch.mall.biz.order.infrastructure.remote.service.CartRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.service.ProductStockRemoteService;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderInfraServiceImpl implements OrderInfraService {

    private final DelayCloseOrderProducer delayCloseOrderProducer;
    private final ProductStockRemoteService productStockRemoteService;
    private final CartRemoteService cartRemoteService;
    private final OrderRepository orderRepository;
    private final CartRemoteClient cartRemoteClient;

    @Override
    public void delayCloseOrderMessageSend(DelayCloseOrderEvent event) {
        try {
            delayCloseOrderProducer.delayCloseOrderSend(event);
        } catch (Exception e) {
            throw new ServiceException(BaseErrorCode.SERVICE_RABBITMQ_ERROR);
        }
    }

    @Override
    public void lockProductStock(OrderCreateEvent event) {
        productStockRemoteService.unlockProductStock(event.getOrder().getOrderSn());
    }

    @Override
    public void clearCartProduct(CartItemDelReqDTO cartItemDelReqDTO) {
        cartRemoteService.clearCartProduct(cartItemDelReqDTO);
    }

    @Override
    public boolean delayCloseOrder(String orderSn) {
        orderRepository.closeOrder(orderSn);                // 1. 关闭订单
        productStockRemoteService.unlockProductStock(orderSn);   // 2. 回滚库存
        // 一般取消订单不会恢复购物车
        return true;
    }

    @Override
    public List<OrderProduct> querySelectCartItemByCustomerUserId(String customerUserId) {
        return cartRemoteService.querySelectCartItemByCustomerUserId(customerUserId);
    }

    @Override
    public Boolean verifyProductStock(List<ProductVerifyStockReqDTO> list) {
        return productStockRemoteService.verifyProductStock(list);
    }
}
