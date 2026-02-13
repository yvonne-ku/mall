

package com.noinch.mall.biz.basicdata.application.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.basicdata.application.resp.RegionInfoRespDTO;
import com.noinch.mall.biz.basicdata.application.service.RegionInfoService;
import com.noinch.mall.biz.basicdata.domain.aggregate.RegionInfo;
import com.noinch.mall.biz.basicdata.domain.repository.RegionInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行政区划接口层实现
 *
 */
@Service
@RequiredArgsConstructor
public class RegionInfoServiceImpl implements RegionInfoService {
    
    private final RegionInfoRepository regionInfoRepository;
    
    @Override
    public List<RegionInfoRespDTO> listAllRegionInfo() {
        List<RegionInfo> regionInfoList = regionInfoRepository.listAllRegionInfo();
        return regionInfoList.stream().map(each -> {
            RegionInfoRespDTO respDTO = new RegionInfoRespDTO();
            BeanUtil.copyProperties(each, respDTO);
            return respDTO;
        }).toList();
    }
    
    @Override
    public List<RegionInfoRespDTO> listRegionInfoByLevel(Integer level) {
        List<RegionInfo> regionInfoList = regionInfoRepository.listRegionInfoByLevel(level);
        return regionInfoList.stream().map(each -> {
            RegionInfoRespDTO respDTO = new RegionInfoRespDTO();
            BeanUtil.copyProperties(each, respDTO);
            return respDTO;
        }).toList();
    }
    
    @Override
    public List<RegionInfoRespDTO> listRegionInfoByCode(String code) {
        List<RegionInfo> regionInfoList = regionInfoRepository.listRegionInfoByCode(code);
        return regionInfoList.stream().map(each -> {
            RegionInfoRespDTO respDTO = new RegionInfoRespDTO();
            BeanUtil.copyProperties(each, respDTO);
            return respDTO;
        }).toList();
    }
    
    @Override
    public List<RegionInfoRespDTO> listRegionInfoByParent(String parent) {
        List<RegionInfo> regionInfoList = regionInfoRepository.listRegionInfoByParent(parent);
        return regionInfoList.stream().map(each -> {
            RegionInfoRespDTO respDTO = new RegionInfoRespDTO();
            BeanUtil.copyProperties(each, respDTO);
            return respDTO;
        }).toList();
    }
}
