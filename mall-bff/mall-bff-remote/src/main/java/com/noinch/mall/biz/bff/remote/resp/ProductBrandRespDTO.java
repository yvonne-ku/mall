package com.noinch.mall.biz.bff.remote.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductBrandRespDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌介绍")
    private String desc;

    @ApiModelProperty("品牌图")
    private String pic;

    @ApiModelProperty("排序")
    private Integer sort;
}