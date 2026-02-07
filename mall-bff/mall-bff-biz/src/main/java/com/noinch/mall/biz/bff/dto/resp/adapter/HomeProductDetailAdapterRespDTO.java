

package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.Data;

import java.util.List;

/**
 * 购物车适配返回对象
 *
 */
@Data
public class HomeProductDetailAdapterRespDTO {
    
    private String detail;
    
    private Integer limitNum;
    
    private String productId;
    
    private String productImageBig;
    
    private List<String> productImageSmall;
    
    private String productName;
    
    private Integer salePrice;
    
    private String subTitle;
}
