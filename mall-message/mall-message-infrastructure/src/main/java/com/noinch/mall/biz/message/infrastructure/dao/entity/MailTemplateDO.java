

package com.noinch.mall.biz.message.infrastructure.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;
import lombok.Data;

/**
 * 邮件模板
 *
 */
@Data
@TableName("mail_template")
public class MailTemplateDO extends BaseDO {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 模板id
     */
    private String templateId;
    
    /**
     * 模板参数
     */
    private String templateParam;
}
