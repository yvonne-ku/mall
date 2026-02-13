

package com.noinch.mall.biz.basicdata.infrastructure.dao.repository;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.noinch.mall.biz.basicdata.infrastructure.dao.dao.entity.RegionInfoDO;
import com.noinch.mall.biz.basicdata.infrastructure.dao.dao.mapper.RegionInfoMapper;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.basicdata.domain.aggregate.RegionInfo;
import com.noinch.mall.biz.basicdata.domain.repository.RegionInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 行政区划仓储层实现
 *
 */
@Repository
@RequiredArgsConstructor
public class RegionInfoRepositoryImpl implements RegionInfoRepository {
    
    private final RegionInfoMapper regionInfoMapper;
    
    @Override
    public List<RegionInfo> listAllRegionInfo() {
        LambdaQueryWrapper<RegionInfoDO> queryWrapper = Wrappers.lambdaQuery(RegionInfoDO.class).select(
                RegionInfoDO::getId,
                RegionInfoDO::getCode,
                RegionInfoDO::getName,
                RegionInfoDO::getParent,
                RegionInfoDO::getLevel,
                RegionInfoDO::getSort);
        List<RegionInfoDO> regionInfoDOList = regionInfoMapper.selectList(queryWrapper);
        return regionInfoDOList.stream().map(each -> {
            RegionInfo regionInfo = new RegionInfo();
            BeanUtil.copyProperties(each, regionInfo);
            return regionInfo;
        }).toList();
    }
    
    @Override
    public List<RegionInfo> listRegionInfoByLevel(Integer level) {
        LambdaQueryWrapper<RegionInfoDO> queryWrapper = Wrappers.lambdaQuery(RegionInfoDO.class)
                .eq(RegionInfoDO::getLevel, level)
                .select(
                        RegionInfoDO::getId,
                        RegionInfoDO::getCode,
                        RegionInfoDO::getName,
                        RegionInfoDO::getParent,
                        RegionInfoDO::getLevel,
                        RegionInfoDO::getSort);
        List<RegionInfoDO> regionInfoDOList = regionInfoMapper.selectList(queryWrapper);
        return regionInfoDOList.stream().map(each -> {
            RegionInfo regionInfo = new RegionInfo();
            BeanUtil.copyProperties(each, regionInfo);
            return regionInfo;
        }).toList();
    }
    
    @Override
    public List<RegionInfo> listRegionInfoByCode(String code) {
        LambdaQueryWrapper<RegionInfoDO> queryWrapper = Wrappers.lambdaQuery(RegionInfoDO.class)
                .eq(RegionInfoDO::getCode, code)
                .select(
                        RegionInfoDO::getId,
                        RegionInfoDO::getCode,
                        RegionInfoDO::getName,
                        RegionInfoDO::getParent,
                        RegionInfoDO::getLevel,
                        RegionInfoDO::getSort);
        List<RegionInfoDO> regionInfoDOList = regionInfoMapper.selectList(queryWrapper);
        return regionInfoDOList.stream().map(each -> {
            RegionInfo regionInfo = new RegionInfo();
            BeanUtil.copyProperties(each, regionInfo);
            return regionInfo;
        }).toList();
    }
    
    @Override
    public List<RegionInfo> listRegionInfoByParent(String parent) {
        LambdaQueryWrapper<RegionInfoDO> queryWrapper = Wrappers.lambdaQuery(RegionInfoDO.class)
                .eq(RegionInfoDO::getParent, parent)
                .select(
                        RegionInfoDO::getId,
                        RegionInfoDO::getCode,
                        RegionInfoDO::getName,
                        RegionInfoDO::getParent,
                        RegionInfoDO::getLevel,
                        RegionInfoDO::getSort);
        List<RegionInfoDO> regionInfoDOList = regionInfoMapper.selectList(queryWrapper);
        return regionInfoDOList.stream().map(each -> {
            RegionInfo regionInfo = new RegionInfo();
            BeanUtil.copyProperties(each, regionInfo);
            return regionInfo;
        }).toList();
    }
}
