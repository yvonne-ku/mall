package com.noinch.mall.biz.order.infrastructure.mq.common;


public class RabbitMQConst {

    // 下面是延迟关闭订单
    /**
     * 业务交换机
     */
    public static final String ORDER_EXCHANGE = "order.exchange";

    /**
     * 延迟订单路由键
     */
    public static final String ORDER_DELAY_ROUTING_KEY = "order.delay";

    /**
     * 延迟队列
     */
    public static final String ORDER_DELAY_QUEUE = "order.delay.queue";

    /**
     * 死信交换机
     */
    public static final String ORDER_DLX_EXCHANGE = "order.dlx.exchange";

    /**
     * 业务队列
     */
    public static final String ORDER_CLOSE_QUEUE = "order.close.queue";

    /**
     * 延迟路由键
     */
    public static final String DELAY_ROUTING_KEY = "order.delay";

    /**
     * 关闭路由键
     */
    public static final String CLOSE_ROUTING_KEY = "order.close";


    // 下面是 PAY_RESULT_NOTIFY
    /**
     * 业务交换机
     */
    public static final String PAY_EXCHANGE = "pay.exchange";

    /**
     * 业务队列
     */
    public static final String PAY_RESULT_NOTIFY_QUEUE = "pay.result.notify.queue";

    /**
     * 路由键
     */
    public static final String  PAY_RESULT_NOTIFY_ROUTING_KEY = "pay.result.notify";
}
