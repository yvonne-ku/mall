

package com.noinch.mall.biz.basicdata.application.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 行政区划查询
 *
 */
@Data
public class RegionInfoListQuery {
    
    @Schema(description = "行政区划编号")
    private String code;
    
    @Schema(description = "行政区划名称")
    private String name;
    
    @Schema(description = "上级行政区划")
    private String parent;
    
    @Schema(description = "层级")
    private Integer level;
}
