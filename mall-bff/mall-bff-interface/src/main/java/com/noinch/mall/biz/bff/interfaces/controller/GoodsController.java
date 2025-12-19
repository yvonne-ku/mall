

package com.noinch.mall.biz.bff.interfaces.controller;

import com.noinch.mall.biz.bff.common.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomePanelAdapterRespDTO;
import com.noinch.mall.biz.bff.service.GoodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商城首页控制层
 *
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "商城首页")
public class GoodsController {
    
    private final GoodsService goodsService;
    
    @GetMapping("/goods/home")
    @ApiOperation(value = "商城首页板块商品数据", notes = "商城首页板块商品数据")
    public ResultT<List<HomePanelAdapterRespDTO>> goodsHome() {
        return ResultT.success(goodsService.listHomePanel());
    }

}
