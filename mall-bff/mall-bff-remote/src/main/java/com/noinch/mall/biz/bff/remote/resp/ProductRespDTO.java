package com.noinch.mall.biz.bff.remote.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ProductRespDTO {

    @Schema(description = "商品品牌")
    private ProductBrandRespDTO productBrand;

    @Schema(description = "商品 SPU")
    private ProductSpuRespDTO productSpu;

    @Schema(description = "商品 SKU")
    private List<ProductSkuRespDTO> productSkus;
}
