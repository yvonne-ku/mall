
package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.Data;

/**
 * 订单商品适配返回对象
 *
 */
@Data
public class OrderGoodsAdapterRespDTO {
    
    private Integer checked;
    
    private Integer limitNum;
    
    private String productId;
    
    private String productImg;
    
    private String productName;
    
    private Integer productNum;
    
    private Integer salePrice;
}
