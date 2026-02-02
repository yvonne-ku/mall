package com.noinch.mall.biz.order.infrastructure.mq.producer;

import com.noinch.mall.biz.order.domain.dto.ProductSkuStockDTO;
import com.noinch.mall.biz.order.domain.event.DelayCloseOrderEvent;
import com.noinch.mall.biz.order.infrastructure.mq.config.DelayCloseOrderMQConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * 延迟关闭订单生产者
 */
@Slf4j
@Component
@AllArgsConstructor
public class DelayCloseOrderProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 延迟发送订单关闭消息
     *
     */
    public void delayCloseOrderSend(DelayCloseOrderEvent event) {
        try {
            // DelayCloseOrderEvent 延迟关闭订单事件
            DelayCloseOrderEvent delayEvent = new DelayCloseOrderEvent(
                    event.getOrderSn(),
                    event.getProductSkuStockList().stream()
                            .map(each -> new ProductSkuStockDTO(String.valueOf(each.getProductId()), String.valueOf(each.getProductSkuId()), each.getProductQuantity()))
                            .collect(Collectors.toList())
            );

            // 发送到业务交换机 -> 延迟队列 -> 死信交换机 -> 业务队列 -> 消费者
            rabbitTemplate.convertAndSend(
                    DelayCloseOrderMQConfig.ORDER_EXCHANGE,
                    DelayCloseOrderMQConfig.DELAY_ROUTING_KEY,
                    delayEvent,
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
            log.error("发送延迟队列取消未付款订单失败", e);
        }
    }
}
