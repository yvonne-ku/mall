
package com.noinch.mall.biz.message.domain.entity;

import com.noinch.mall.ddd.framework.core.domain.AggregateRoot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 消息发送查询 Entity
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendQuery implements AggregateRoot {
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 消息发送 ID 集合
     */
    private List<Long> messageSendIdList;
    
    /**
     * 接收者集合
     */
    private List<String> receiverList;
    
    /**
     * 消息发送 ID
     */
    private Long messageSendId;
    
    /**
     * 接收者
     */
    private String receiver;
    
    /**
     * 消息状态: 0：发送成功 1：发送失败 2：发送中 3：提交失败
     */
    private Integer status;
}
