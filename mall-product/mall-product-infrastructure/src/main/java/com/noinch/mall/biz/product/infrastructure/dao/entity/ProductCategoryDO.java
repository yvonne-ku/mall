

package com.noinch.mall.biz.product.infrastructure.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;


/**
 * 商品分类
 */
@Data
@TableName("product_category")
public class ProductCategoryDO extends BaseDO {
    
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 父级id
     */
    private Long parentId;
    
    /**
     * 层级
     */
    private Integer level;
    
    /**
     * 图标url
     */
    private String iconUrl;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 跳转地址
     */
    private String url;
    
    /**
     * 状态 0：展示 1：隐藏
     */
    private Integer status;
}
