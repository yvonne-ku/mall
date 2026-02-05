package com.noinch.mall.biz.pay.infrastructure.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

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

    /**
     * 1. 业务交换机
     */
    @Bean
    public DirectExchange payExchange() {
        return new DirectExchange(PAY_EXCHANGE);
    }

    /**
     * 2. 业务处理队列
     */
    @Bean
    public Queue payResultNotifyQueue() {
        return new Queue(PAY_RESULT_NOTIFY_QUEUE, true);
    }

    /**
     * 绑定
     */
    @Bean
    public Binding payResultNotifyBinding() {
        return BindingBuilder.bind(payResultNotifyQueue())
                .to(payExchange())
                .with(PAY_RESULT_NOTIFY_ROUTING_KEY);
    }
}
