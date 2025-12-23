package com.noinch.mall.biz.bff.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;
import lombok.Data;

/**
 * 板块商品关联实体
 *
 */
@Data
@TableName("panel_product_relation_test")
public class PanelProductRelationDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 属于的 panel id
     */
    private Long panelId;

    /**
     * 商品 spu id
     */
    private Long productId;

    /**
     * 商品大图
     */
    private String bigPic;

    /**
     * 商品图
     */
    private String pic;

    /**
     * 商品在 panel 中的顺序
     */
    private Integer sort;
}
