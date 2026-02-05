package com.noinch.mall.biz.order.infrastructure.mq.producer;

import com.noinch.mall.biz.order.domain.event.DelayCloseOrderEvent;
import com.noinch.mall.biz.order.infrastructure.mq.common.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 延迟关闭订单生产者
 */
@Slf4j
@Component
public class DelayCloseOrderProducer {

    @Autowired
    @Qualifier("customRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 延迟发送订单关闭消息
     *
     */
    public void delayCloseOrderSend(DelayCloseOrderEvent event) {
        try {
            // 发送到业务交换机 -> 延迟队列 -> 死信交换机 -> 业务队列 -> 消费者
            rabbitTemplate.convertAndSend(
                    RabbitMQConst.ORDER_EXCHANGE,
                    RabbitMQConst.DELAY_ROUTING_KEY,
                    event,
                    message -> {
                        // 1. 设置唯一消息 ID
                        String messageId = "DELAY_ORDER_" + event.getOrderSn();
                        message.getMessageProperties().setMessageId(messageId);

                        // 2. 设置消息级过期时间 (TTL)
                        // 1800000 毫秒 = 30 分钟
                        message.getMessageProperties().setExpiration("1800000");

                        // 3. (可选) 设置消息持久化
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return message;
                    }
            );
            log.info("延迟关单消息发送成功, 订单号: {}, 消息ID: {}", event.getOrderSn(), "DELAY_ORDER_" + event.getOrderSn());
        } catch (Exception e) {
            log.error("延迟关单消息发送失败", e);
        }
    }
}
