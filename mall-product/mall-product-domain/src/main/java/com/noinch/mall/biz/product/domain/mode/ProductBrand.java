

package com.noinch.mall.biz.product.domain.mode;

import lombok.Data;

/**
 * 商品品牌
 */
@Data
public class ProductBrand {
    
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
    private String desc;
    
    /**
     * 品牌图
     */
    private String pic;
    
    /**
     * 排序
     */
    private Integer sort;
}
