package com.noinch.mall.biz.message.infrastructure.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息发送扩展记录 DO
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("send_record_extend")
public class SendRecordExtendDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 消息发送id
     */
    private String msgId;

    /**
     * 发送参数
     */
    private String msgParam;

    /**
     * 消息文本
     */
    private String msgContent;
}
