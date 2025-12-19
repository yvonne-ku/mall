package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.Data;

import java.util.Date;

/**
 * 首页板块内容适配返回对象
 *
 */
@Data
public class HomePanelContentAdapterRespDTO {
    
    /**
     * id
     */
    private String id;

    /**
     * 模块ID
     */
    private Integer panelId;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 图片地址
     */
    private String picUrl;
    
    /**
     * 图片地址
     */
    private String picUrl2;
    
    /**
     * 图片地址
     */
    private String picUrl3;

    /**
     * 地址
     */
    private String fullUrl;

    /**
     * 商品ID
     */
    private String productId;
    
    /**
     * 商品大图
     */
    private String productImageBig;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 销售价格
     */
    private Integer salePrice;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 商品标题
     */
    private String subTitle;
    
    /**
     * 创建时间
     */
    private Date created;
    
    /**
     * 修改时间
     */
    private Date updated;
}
