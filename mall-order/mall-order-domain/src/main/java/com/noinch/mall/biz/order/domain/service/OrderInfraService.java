package com.noinch.mall.biz.order.domain.service;

import com.noinch.mall.biz.order.domain.event.DelayCloseOrderEvent;

public interface OrderInfraService {

    void delayCloseOrderMessageSend(DelayCloseOrderEvent event);
}
