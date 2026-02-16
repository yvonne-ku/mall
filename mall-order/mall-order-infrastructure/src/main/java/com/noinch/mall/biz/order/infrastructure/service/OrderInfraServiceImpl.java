package com.noinch.mall.biz.order.infrastructure.service;

import com.noinch.mall.biz.order.domain.event.DelayCloseOrderEvent;
import com.noinch.mall.biz.order.domain.service.OrderInfraService;
import com.noinch.mall.biz.order.infrastructure.mq.producer.DelayCloseOrderProducer;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderInfraServiceImpl implements OrderInfraService {

    private final DelayCloseOrderProducer delayCloseOrderProducer;

    @Override
    public void delayCloseOrderMessageSend(DelayCloseOrderEvent event) {
        try {
            delayCloseOrderProducer.delayCloseOrderSend(event);
        } catch (Exception e) {
            throw new ServiceException(BaseErrorCode.SERVICE_RABBITMQ_ERROR);
        }
    }
}
