

package com.noinch.mall.biz.basicdata.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.basicdata.application.resp.RegionInfoRespDTO;
import com.noinch.mall.biz.basicdata.application.service.RegionInfoService;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.log.annotation.MLog;
import com.noinch.mall.springboot.starter.web.Results;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行政区划控制层
 *
 */
@RestController
@Tag(name = "行政区划")
@RequiredArgsConstructor
public class RegionInfoController {
    
    private final RegionInfoService regionInfoService;
    
    @MLog(output = false)
    @GetMapping("/api/basics-data/region/all")
    @Operation(description = "查询行政区划数据")
    public Result<List<RegionInfoRespDTO>> listAllRegionInfo() {
        return Results.success(regionInfoService.listAllRegionInfo());
    }
    
    @MLog(output = false)
    @GetMapping("/api/basics-data/region/list/level/{level}")
    @Operation(description = "根据层级查询行政区划数据")
    @Parameters({
            @Parameter(name = "level", description = "层级", example = "1")
    })
    public Result<List<RegionInfoRespDTO>> listRegionInfoByLevel(@PathVariable("level") Integer level) {
        return Results.success(regionInfoService.listRegionInfoByLevel(level));
    }
    
    @MLog(output = false)
    @GetMapping("/api/basics-data/region/list/code/{code}")
    @Operation(description = "查询行政区划数据")
    @Parameters({
            @Parameter(name = "code", description = "行政区划编号", example = "110000")
    })
    public Result<List<RegionInfoRespDTO>> listRegionInfoByCode(@PathVariable("code") String code) {
        return Results.success(regionInfoService.listRegionInfoByCode(code));
    }
    
    @MLog(output = false)
    @GetMapping("/api/basics-data/region/list/parent/{parent}")
    @Operation(description = "根据上级行政区划编码查询行政区划数据")
    @Parameters({
            @Parameter(name = "parent", description = "上级行政区划编码", example = "CN0001")
    })
    public Result<List<RegionInfoRespDTO>> listRegionInfoByParent(@PathVariable("parent") String parent) {
        return Results.success(regionInfoService.listRegionInfoByParent(parent));
    }
}
