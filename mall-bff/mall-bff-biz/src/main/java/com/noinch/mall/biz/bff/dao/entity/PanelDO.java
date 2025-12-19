package com.noinch.mall.biz.bff.dao.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;
import lombok.Data;

/**
 * 板块实体
 *
 */
@Data
@TableName("panel")
public class PanelDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;

    /**
     * 位置
     */
    private Integer position;

    /**
     * 限制数量
     */
    private Integer limitNum;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
