package com.noinch.mall.biz.bff.remote.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductBrandRespDTO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "品牌名称")
    private String name;

    @Schema(description = "品牌介绍")
    private String desc;

    @Schema(description = "品牌图")
    private String pic;

    @Schema(description = "排序")
    private Integer sort;
}