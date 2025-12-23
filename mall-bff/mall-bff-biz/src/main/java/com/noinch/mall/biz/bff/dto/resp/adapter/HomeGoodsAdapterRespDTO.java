
package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.Data;

/**
 * 商品适配返回对象
 *
 */
@Data
public class HomeGoodsAdapterRespDTO {
    
    private String productId;
    
    private String productImageBig;
    
    private String productName;
    
    private Integer salePrice;
    
    private String subTitle;
}
