

package com.noinch.mall.biz.product.application.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品分类
 * */
@Data
public class ProductCategoryRespDTO {
    
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "父级ID")
    private Long parentId;
    
    @Schema(description = "层级")
    private Integer level;
    
    @Schema(description = "图标URL")
    private String iconUrl;
    
    @Schema(description = "排序")
    private Integer sort;
    
    @Schema(description = "跳转地址")
    private String url;
}
