

package com.noinch.mall.biz.order.domain.event;

import lombok.Getter;
import com.noinch.mall.biz.order.domain.aggregate.Order;
import org.springframework.context.ApplicationEvent;

/**
 * 订单创建事件
 *
 */
public final class OrderCreateEvent extends ApplicationEvent {
    
    /**
     * 订单聚合根
     */
    @Getter
    private Order order;
    
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public OrderCreateEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}
