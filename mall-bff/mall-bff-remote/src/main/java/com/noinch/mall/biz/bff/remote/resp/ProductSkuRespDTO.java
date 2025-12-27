
package com.noinch.mall.biz.bff.remote.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品 SKU 出参
 *
 */
@Data
public class ProductSkuRespDTO {
    
    @Schema(description = "id")
    private Long id;
    
    @Schema(description = "商品 id")
    private Long productId;
    
    @Schema(description = "价格")
    private BigDecimal price;
    
    @Schema(description = "库存")
    private Integer stock;
    
    @Schema(description = "锁定库存")
    private Integer lockStock;
    
    @Schema(description = "图片")
    private String pic;
    
    @Schema(description = "属性，json 格式")
    private String attribute;
}
