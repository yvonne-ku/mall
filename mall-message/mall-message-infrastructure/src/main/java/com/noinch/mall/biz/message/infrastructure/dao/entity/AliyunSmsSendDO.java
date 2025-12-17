package com.noinch.mall.biz.message.infrastructure.dao.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Aliyun 短信发送实体 */
@Data
@Accessors(chain = true)
@TableName("message_aliyun_sms_send_test")
public class AliyunSmsSendDO {

    /**
     * id
     */
    private Long id;

    /**
     * 手机号
     */
    private String phoneNumber;

     /**
     * 短信发送时间
     */
    private DateTime sendTime;

    /**
     * 短信发送状态
     */
    private String status;
}
