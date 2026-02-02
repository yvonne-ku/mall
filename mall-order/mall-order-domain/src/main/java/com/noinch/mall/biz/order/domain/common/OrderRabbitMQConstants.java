package com.noinch.mall.biz.order.domain.common;

/**
 * 订单 RabbitMQ 常量
 */
public class OrderRabbitMQConstants {
    /**
     * 延迟关闭订单交换机
     */
    public static final String ORDER_DELAY_EXCHANGE = "order.delay.exchange";

    /**
     * 延迟关闭订单路由键
     */
    public static final String ORDER_DELAY_ROUTING_KEY = "order.delay.routingKey";
}
