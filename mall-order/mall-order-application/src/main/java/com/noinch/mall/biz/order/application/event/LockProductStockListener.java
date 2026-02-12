
package com.noinch.mall.biz.order.application.event;

import com.noinch.mall.biz.order.domain.event.OrderCreateEvent;
import com.noinch.mall.biz.order.domain.service.ProductStockRemoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 锁定商品库存监听
 *
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public final class LockProductStockListener implements ApplicationListener<OrderCreateEvent> {
    
    private final ProductStockRemoteService productStockRemoteService;

    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        productStockRemoteService.lockProductStock(event);
    }
}
