package com.noinch.mall.biz.message.domain.repository;

import com.noinch.mall.biz.message.domain.entity.MessageSend;

public interface MessageRepository {

    void save(MessageSend messageSend);

}