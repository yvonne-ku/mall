package com.noinch.mall.biz.order.infrastructure.mq.consumer;

import com.noinch.mall.biz.order.domain.aggregate.Order;
import com.noinch.mall.biz.order.domain.common.OrderStatusEnum;
import com.noinch.mall.biz.order.infrastructure.mq.common.RabbitMQConst;
import com.noinch.mall.biz.order.infrastructure.mq.common.TradeStatusEnum;
import com.noinch.mall.biz.order.infrastructure.mq.dto.PayResultNotifyMessageEvent;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
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
public class PayResultNotifyConsumer {

    private final OrderRepository orderRepository;


    @RabbitListener(queues = RabbitMQConst.PAY_RESULT_NOTIFY_QUEUE)
    public void onMessage(PayResultNotifyMessageEvent event,
                          Channel channel,
                          Message message,
                          @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                          @Header(AmqpHeaders.MESSAGE_ID) String messageId) throws IOException {

        log.info("收到支付结果通知消息, 订单号: {}, MessageID: {}", event.getOrderSn(), messageId);

        try {
            // 1. 幂等性检查，检查这个订单的状态是不是待处理
            if (orderRepository.findOrderByOrderSn(event.getOrderSn()).getStatus() != OrderStatusEnum.PENDING_PAYMENT.getStatus()) {
                log.info("订单 {} 已处理，跳过重复消息", event.getOrderSn());
                channel.basicAck(tag, false); // 状态错误，确认签收，从队列删除
                return;
            }

            // 2. 修改订单状态
            Order updateOrder = new Order();
            updateOrder.setOrderSn(event.getOrderSn());
            if (event.getStatus().equals(TradeStatusEnum.TRADE_SUCCESS.name())) {
                updateOrder.setStatus(OrderStatusEnum.TO_BE_DELIVERED.getStatus());
            }
            updateOrder.setStatus(Integer.valueOf(event.getStatus()));
            orderRepository.updateOrderStatus(updateOrder);
            channel.basicAck(tag, false); // 处理完成，确认签收，从队列删除

        } catch (Exception e) {
            log.error("处理支付状态变化失败，订单号: {}", event.getOrderSn(), e);
            channel.basicNack(tag, false, true);
        }
    }
}
