

package com.noinch.mall.biz.bff.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.noinch.mall.biz.bff.dao.entity.PanelProductRelationDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 板块商品关联持久层
 *
 */
public interface PanelProductRelationMapper extends BaseMapper<PanelProductRelationDO> {
    
    /**
     * 根据板块 ID 查询关联记录
     */
    List<PanelProductRelationDO> listPanelProductRelationByPanelId(@Param("panelId") Integer panelId);
}
