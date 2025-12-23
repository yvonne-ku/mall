package com.noinch.mall.biz.bff.remote.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductRespDTO {

    @ApiModelProperty(value = "商品品牌")
    private ProductBrandRespDTO productBrand;

    @ApiModelProperty(value = "商品 SPU")
    private ProductSpuRespDTO productSpu;

    @ApiModelProperty(value = "商品 SKU")
    private List<ProductSkuRespDTO> productSkus;
}
