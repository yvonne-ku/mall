package com.noinch.mall.biz.order.infrastructure.service;

import com.noinch.mall.biz.order.domain.aggregate.Order;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.dto.CartItemDelReqDTO;
import com.noinch.mall.biz.order.domain.dto.ProductVerifyStockReqDTO;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import com.noinch.mall.biz.order.domain.service.OrderInfraService;
import com.noinch.mall.biz.order.infrastructure.mq.producer.DelayCloseOrderProducer;
import com.noinch.mall.biz.order.infrastructure.remote.service.CartRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.service.ProductStockRemoteService;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderInfraServiceImpl implements OrderInfraService {

    private final DelayCloseOrderProducer delayCloseOrderProducer;
    private final ProductStockRemoteService productStockRemoteService;
    private final CartRemoteService cartRemoteService;
    private final OrderRepository orderRepository;

    @Override
    public void delayCloseOrderMessageSend(Order order) {
        try {
            delayCloseOrderProducer.delayCloseOrderSend(order);
        } catch (Exception e) {
            throw new ServiceException(BaseErrorCode.SERVICE_RABBITMQ_ERROR);
        }
    }

    @Override
    public void lockProductStock(Order order) {
        productStockRemoteService.lockProductStock(order);
    }

    @Override
    public void clearCartProduct(Order order) {
        CartItemDelReqDTO dto = CartItemDelReqDTO.builder()
                .customerUserId(String.valueOf(order.getCustomerUserId()))
                .productSkuIds(order.getOrderProducts().stream().map(OrderProduct::getProductSkuId).map(String::valueOf).collect(Collectors.toList()))
                .build();
        cartRemoteService.clearCartProduct(dto);
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
