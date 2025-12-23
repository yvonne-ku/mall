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
@TableName("panel_test")
public class PanelDO extends BaseDO {

    /**
     * panel id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * panel 名称
     */
    private String name;

    /**
     * panel 位置
     */
    private Integer position;

    /**
     * 限制数量
     */
    private Integer limitNum;

    /**
     * panel 显示顺序
     */
    private Integer sortOrder;

    /**
     * 状态
     */
    private Integer status;

    /**
     * panel 类型
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;
}
