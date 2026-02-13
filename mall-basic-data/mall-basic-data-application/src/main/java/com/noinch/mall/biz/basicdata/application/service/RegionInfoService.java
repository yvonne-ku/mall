
package com.noinch.mall.biz.basicdata.application.service;

import com.noinch.mall.biz.basicdata.application.resp.RegionInfoRespDTO;

import java.util.List;

/**
 * 行政区划接口层
 *
 */
public interface RegionInfoService {
    
    /**
     * 查询行政区划数据
     *
     * @return 全部行政区划数据
     */
    List<RegionInfoRespDTO> listAllRegionInfo();
    
    /**
     * 根据层级查询行政区划数据
     *
     * @param level 层级，比如省市区三级，eg：1、2、3
     * @return 行政区划数据
     */
    List<RegionInfoRespDTO> listRegionInfoByLevel(Integer level);
    
    /**
     * 根据编码查询行政区划数据
     *
     * @param code 行政区划编码，比如北京：110000
     * @return 行政区划数据
     */
    List<RegionInfoRespDTO> listRegionInfoByCode(String code);
    
    /**
     * 根据上级行政区划编码查询行政区划数据
     *
     * @param parent 上级行政区划编码，比如北京：110000
     * @return 行政区划数据
     */
    List<RegionInfoRespDTO> listRegionInfoByParent(String parent);
}
