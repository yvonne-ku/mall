

package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.Data;

/**
 * 购物车适配返回对象
 *
 */
@Data
public class CartAdapterRespDTO {
    
    private String checked;
    
    private Integer limitNum;
    
    private String productId;
    
    private String productImg;
    
    private String productName;
    
    private Integer productNum;
    
    private Integer salePrice;
}
