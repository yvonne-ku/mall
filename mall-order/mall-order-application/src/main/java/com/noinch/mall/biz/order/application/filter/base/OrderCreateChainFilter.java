
package com.noinch.mall.biz.order.application.filter.base;

import com.noinch.mall.biz.order.application.enums.OrderChainMarkEnum;
import com.noinch.mall.biz.order.application.req.OrderCreateCommand;
import com.noinch.mall.springboot.starter.designpattern.chain.AbstractChainHandler;

/**
 * 订单创建责任链过滤器
 *
 */
public interface OrderCreateChainFilter<T extends OrderCreateCommand> extends AbstractChainHandler<OrderCreateCommand> {
    
    @Override
    default String mark() {
        return OrderChainMarkEnum.ORDER_CREATE_FILTER.name();
    }
}
