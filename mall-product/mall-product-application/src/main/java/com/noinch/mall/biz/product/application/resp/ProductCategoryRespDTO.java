

package com.noinch.mall.biz.product.application.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品分类
 * */
@Data
public class ProductCategoryRespDTO {
    
    @ApiModelProperty(value = "分类名称")
    private String name;
    
    @ApiModelProperty(value = "父级ID")
    private Long parentId;
    
    @ApiModelProperty(value = "层级")
    private Integer level;
    
    @ApiModelProperty(value = "图标URL")
    private String iconUrl;
    
    @ApiModelProperty(value = "排序")
    private Integer sort;
    
    @ApiModelProperty(value = "跳转地址")
    private String url;
}
