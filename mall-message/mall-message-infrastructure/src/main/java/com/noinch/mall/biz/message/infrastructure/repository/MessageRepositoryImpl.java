package com.noinch.mall.biz.message.infrastructure.repository;

import cn.hutool.core.bean.BeanUtil;
import com.noinch.mall.biz.message.domain.entity.MessageSend;
import com.noinch.mall.biz.message.domain.repository.MessageRepository;
import com.noinch.mall.biz.message.infrastructure.dao.entity.SendRecordDO;
import com.noinch.mall.biz.message.infrastructure.dao.mapper.SendRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

    private final SendRecordMapper sendRecordMapper;

    @Override
    public void save(MessageSend messageSend) {
        SendRecordDO sendRecordDO = new SendRecordDO();
        BeanUtil.copyProperties(messageSend, sendRecordDO);
        sendRecordDO.setStatus(messageSend.getStatus() ? 1 : 0);
        sendRecordDO.setSendTime(new Date());
        sendRecordMapper.insert(sendRecordDO);
    }
}
