package com.noinch.mall.biz.bff.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.noinch.mall.biz.bff.dao.entity.PanelDO;

import java.util.List;

/**
 * 板块持久层
 *
 */
public interface PanelMapper extends BaseMapper<PanelDO> {

    /**
     * 查询所有板块记录
     */
    List<PanelDO> listAllPanel();

    /**
     * 为您推荐板块
     */
    PanelDO getRecommend();
}
