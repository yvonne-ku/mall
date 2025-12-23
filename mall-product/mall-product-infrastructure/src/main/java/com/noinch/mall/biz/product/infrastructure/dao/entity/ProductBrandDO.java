

package com.noinch.mall.biz.product.infrastructure.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;


/**
 * 商品品牌
 */
@Data
@TableName("product_brand_test")
public class ProductBrandDO extends BaseDO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 品牌名称
     */
    private String name;
    
    /**
     * 品牌介绍
     */
    @TableField("`desc`")
    private String desc;
    
    /**
     * 品牌图
     */
    @TableField("`pic`")
    private String pic;
    
    /**
     * 排序
     */
    @TableField("`sort`")
    private Integer sort;
}
