

package com.noinch.mall.biz.basicdata.infrastructure.dao.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;
import lombok.Data;

/**
 * 行政区划实体
 *
 */
@Data
@TableName("region_info")
public class RegionInfoDO extends BaseDO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 行政区划编号
     */
    private String code;
    
    /**
     * 行政区划名称
     */
    private String name;
    
    /**
     * 上级行政区划
     */
    private String parent;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 层级
     */
    private Integer level;
}
