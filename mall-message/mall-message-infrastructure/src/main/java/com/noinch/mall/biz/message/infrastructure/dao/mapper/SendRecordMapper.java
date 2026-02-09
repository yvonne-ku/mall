

package com.noinch.mall.biz.message.infrastructure.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.noinch.mall.biz.message.infrastructure.dao.entity.SendRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息发送记录 Mapper
 *
 */
@Mapper
public interface SendRecordMapper extends BaseMapper<SendRecordDO> {
}
