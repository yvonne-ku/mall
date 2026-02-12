

package com.noinch.mall.biz.order.application.event;

import com.noinch.mall.biz.order.domain.event.OrderCreateEvent;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 订单创建监听
 *
 */
@Order(0)
@Component
@RequiredArgsConstructor
public final class OrderCreateListener implements ApplicationListener<OrderCreateEvent> {
    
    private final OrderRepository orderRepository;
    
    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        orderRepository.createOrder(event.getOrder());
    }
}
