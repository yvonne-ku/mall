package com.noinch.mall.biz.order.infrastructure.mq.consumer;


import com.noinch.mall.biz.order.domain.common.OrderStatusEnum;
import com.noinch.mall.biz.order.domain.event.DelayCloseOrderEvent;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import com.noinch.mall.biz.order.infrastructure.mq.common.RabbitMQConst;
import com.noinch.mall.biz.order.infrastructure.service.OrderInfraService;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@AllArgsConstructor
public class DelayCloseOrderConsumer {

    private final OrderInfraService orderInfraService;
    private final OrderRepository orderRepository;

    /**
     * 监听延迟关单队列
     * @param event 延迟关单事件
     * @param channel AMQP 通道
     * @param message 原始消息对象
     * @param tag     消息的唯一投递表示，在当前 Channel 上唯一
     * @param messageId 在发送端手动设置的唯一消息 ID
     */
    @RabbitListener(queues = RabbitMQConst.ORDER_CLOSE_QUEUE)
    public void onMessage(DelayCloseOrderEvent event,
                          Channel channel,
                          Message message,
                          @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                          @Header(AmqpHeaders.MESSAGE_ID) String messageId) throws IOException {

        log.info("收到延迟关单消息: {}, MessageID: {}", event.getOrderSn(), messageId);

        try {
            // 1. 幂等性检查，检查这个订单的状态是不是待处理
            if (orderRepository.findOrderByOrderSn(event.getOrderSn()).getStatus() != OrderStatusEnum.PENDING_PAYMENT.getStatus()) {
                log.info("订单 {} 已处理，跳过重复消息", event.getOrderSn());
                channel.basicAck(tag, false); // 状态错误，确认签收，从队列删除
                return;
            }

            // 2. 取消订单，回滚库存
            boolean success = orderInfraService.delayCloseOrder(event.getOrderSn());
            if (!success) {
                log.error("订单 {} 关单失败", event.getOrderSn());
                channel.basicAck(tag, false); // 关单失败，确认签收，从队列删除
                return;
            }
            log.info("订单 {} 关单成功", event.getOrderSn());
            channel.basicAck(tag, false);   // 业务成功，手动签收

        } catch (Exception e) {
        log.error("处理延迟关单异常，订单号: {}", event.getOrderSn(), e);

        // 4. 发生系统异常，拒绝签收
        // 第三个参数 requeue = true 表示重回队列头部重新消费
        // 生产建议：如果重试多次依然失败，建议存入数据库人工处理，而不是无限死循环重试
        channel.basicNack(tag, false, true);
        }
    }
}
