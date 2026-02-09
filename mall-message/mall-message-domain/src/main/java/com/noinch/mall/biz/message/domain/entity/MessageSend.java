
package com.noinch.mall.biz.message.domain.entity;

import lombok.*;

import java.util.List;

/**
 * 消息发送 Entity
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class MessageSend {
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 发送者
     */
    private String sender;
    
    /**
     * 接收者
     */
    private String receiver;
    
    /**
     * 抄送
     */
    private String cc;
    
    /**
     * 消息参数
     */
    private List<String> paramList;
    
    /**
     * 模板ID
     */
    private String templateId;
    
    /**
     * 消息发送ID
     */
    private String messageSendId;
    
    /**
     * 发送结果
     */
    private Boolean status;
    
    /**
     * 模板类型 0：短信-验证码 1：短信-通知 2：短信-营销 3：微信模板消息 4：邮箱 5...
     */
    private Integer msgType;
}
