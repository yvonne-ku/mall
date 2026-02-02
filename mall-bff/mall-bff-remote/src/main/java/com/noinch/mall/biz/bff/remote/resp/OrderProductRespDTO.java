
package com.noinch.mall.biz.bff.remote.resp;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品出参
 *
 */
@Data
public class OrderProductRespDTO {
    
    /**
     * 订单id
     */
    private String orderId;
    
    /**
     * 订单编号
     */
    private String orderSn;
    
    /**
     * 商品 SPU ID
     */
    private String productId;
    
    /**
     * 商品 SKU ID
     */
    private String productSkuId;
    
    /**
     * 商品图
     */
    private String productPic;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品品牌
     */
    private String productBrand;
    
    /**
     * 商品价格
     */
    private BigDecimal productPrice;
    
    /**
     * 商品购买数量
     */
    private Integer productQuantity;
    
    /**
     * 规格，json 格式
     */
    private String productAttribute;
}
