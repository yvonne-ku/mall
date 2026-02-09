

package com.noinch.mall.biz.message.infrastructure.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.noinch.mall.biz.message.infrastructure.dao.entity.MailTemplateDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件模板
 *
 */
@Mapper
public interface MailTemplateMapper extends BaseMapper<MailTemplateDO> {
}
