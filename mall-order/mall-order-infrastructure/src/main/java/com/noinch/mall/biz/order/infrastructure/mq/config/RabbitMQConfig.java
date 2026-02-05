package com.noinch.mall.biz.order.infrastructure.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.noinch.mall.biz.order.infrastructure.mq.common.RabbitMQConst.*;

@Configuration
public class RabbitMQConfig {


    /**
     * 1. 业务交换机
     * 消息首先发给业务交换机，携带 ORDER_DELAY_ROUTING_KEY，转发给延迟队列
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    /**
     * 2. 延迟队列
     * 接收来自业务交换机的消息，但是没有下游消费者，等待消息过期，然后转给死信交换机
     * 关键参数：
     *  x-message-ttl: 消息存活时间（毫秒），例如15分钟=15*60*1000
     *  x-dead-letter-exchange: 消息到期后转发的死信交换机
     *  x-dead-letter-routing-key: 转发时使用的路由键
     */
    @Bean
    public Queue orderDelayQueue() {
        return QueueBuilder.durable(ORDER_DELAY_QUEUE)
                .withArgument("x-message-ttl", 15 * 60 * 1000) // 15分钟延迟
                .withArgument("x-dead-letter-exchange", ORDER_DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ORDER_DELAY_ROUTING_KEY)
                .build();
    }

    /**
     * 3. 死信交换机
     * 获得过期消息，转发给绑定了消费者的业务队列进行处理
     */
    @Bean
    public DirectExchange orderDlxExchange() {
        return new DirectExchange(ORDER_DLX_EXCHANGE);
    }

    /**
     * 4. 业务处理队列
     * 实际处理订单关闭的队列
     */
    @Bean
    public Queue orderCloseQueue() {
        return new Queue(ORDER_CLOSE_QUEUE, true);
    }

    /**
     * 绑定：将延迟队列绑定到业务交换机
     */
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(orderDelayQueue())
                .to(orderExchange())
                .with(DELAY_ROUTING_KEY);
    }

    /**
     * 绑定：将业务队列绑定到死信交换机
     */
    @Bean
    public Binding closeBinding() {
        return BindingBuilder.bind(orderCloseQueue())
                .to(orderDlxExchange())
                .with(CLOSE_ROUTING_KEY);
    }

}
