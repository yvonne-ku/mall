

package com.noinch.mall.biz.pay.infrastructure.mq.producer;

import com.noinch.mall.biz.pay.infrastructure.mq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.pay.domain.event.PayResultNotifyMessageEvent;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 支付消息通用发送生产者
 *
 */
@Slf4j
@Component
public class PayResultNotifyProducer {

    @Autowired
    @Qualifier("customRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 支付消息通用发送
     *
     * @param event 支付结果消息发送事件
     */
    public void payResultNotifyMessageSend(PayResultNotifyMessageEvent event) {
        try {
            // 发送到业务交换机 -> 业务队列 -> 消费者
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAY_EXCHANGE,
                    RabbitMQConfig.PAY_RESULT_NOTIFY_ROUTING_KEY,
                    event,
                    message -> {
                        // 1. 设置唯一消息 ID
                        String messageId = "PAY_RESULT_NOTIFY" + event.getOrderSn();
                        message.getMessageProperties().setMessageId(messageId);

                        // 2. 设置消息级过期时间 (TTL)
                        // 1800000 毫秒 = 30 分钟
                        message.getMessageProperties().setExpiration("1800000");

                        // 3. (可选) 设置消息持久化
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return message;
                    }
            );
            log.info("支付结果通知消息发送成功, 交易凭证号：{}, 订单号: {}, 消息ID: {}", event.getTradeNo() ,event.getOrderSn(), "PAY_RESULT_NOTIFY" + event.getOrderSn());
        } catch (Exception e) {
            log.error("支付结果通知消息发送失败", e);
        }
    }
}
