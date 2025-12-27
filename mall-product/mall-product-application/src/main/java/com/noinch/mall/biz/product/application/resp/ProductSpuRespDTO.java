

package com.noinch.mall.biz.product.application.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品 SPU 出参
 */
@Data
public class ProductSpuRespDTO {

    @Schema(description = "id")
    private Long id;
    
    @Schema(description = "商品类型id")
    private Long categoryId;
    
    @Schema(description = "商品品牌id")
    private Long brandId;
    
    @Schema(description = "商品名称")
    private String name;
    
    @Schema(description = "商品编码")
    private String productSn;
    
    @Schema(description = "商品主图")
    private String pic;

    @Schema(description = "商品图集")
    private String photoAlbum;
    
    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "促销价格")
    private BigDecimal promotionPrice;
    
    @Schema(description = "促销开始时间")
    private Date promotionStartTime;
    
    @Schema(description = "促销结束时间")
    private Date promotionEndTime;
    
    @Schema(description = "副标题")
    private String subTitle;
    
    @Schema(description = "销量")
    private Integer sales;
    
    @Schema(description = "单位")
    private String unit;
    
    @Schema(description = "商品详情")
    private String detail;
    
    @Schema(description = "发布状态 0：发布 1：未发布")
    private Integer publishStatus;
    
    @Schema(description = "新品状态 0：新品 1：非新品")
    private Integer newStatus;
    
    @Schema(description = "推荐状态 0：推荐 1：非推荐")
    private Integer recommandStatus;
}
