

package com.noinch.mall.biz.order.application.event;

import com.noinch.mall.biz.order.infrastructure.mq.producer.DelayCloseOrderProducer;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.order.domain.dto.ProductSkuStockDTO;
import com.noinch.mall.biz.order.domain.event.DelayCloseOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * 发送延迟队列取消未付款订单监听
 *
 */
@Order(3)
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayCloseOrderListener implements ApplicationListener<OrderCreateEvent> {
    
    private final DelayCloseOrderProducer delayCloseOrderProducer;

    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        DelayCloseOrderEvent delayEvent = new DelayCloseOrderEvent(
                event.getOrder().getOrderSn(),
                event.getOrder().getOrderProducts().stream()
                        .map(each -> new ProductSkuStockDTO(String.valueOf(each.getProductId()), String.valueOf(each.getProductSkuId()), each.getProductQuantity()))
                        .collect(Collectors.toList())
        );
        delayCloseOrderProducer.delayCloseOrderSend(delayEvent);
    }
}
