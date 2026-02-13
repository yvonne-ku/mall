

package com.noinch.mall.biz.basicdata.application.resp;

import lombok.Data;

/**
 * 行政区划返回对象
 *
 */
@Data
public class RegionInfoRespDTO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 行政区划编号
     */
    private String code;
    
    /**
     * 行政区划名称
     */
    private String name;
    
    /**
     * 上级行政区划
     */
    private String parent;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 层级
     */
    private Integer level;
}
