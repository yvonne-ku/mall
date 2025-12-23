

package com.noinch.mall.biz.bff.interfaces.controller;

import com.noinch.mall.biz.bff.common.ResultT;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomeGoodsResultAdapterRespDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomePanelAdapterRespDTO;
import com.noinch.mall.biz.bff.service.HomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商城首页控制层
 *
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "商城首页")
@RequestMapping("/api/home")
public class HomeController {
    
    private final HomeService homeService;
    
    @GetMapping("/panel")
    @ApiOperation(value = "商城首页板块商品数据", notes = "商城首页板块商品数据")
    public ResultT<List<HomePanelAdapterRespDTO>> homePanel() {
        return ResultT.success(homeService.listHomePanel());
    }

    @GetMapping("/allGoods")
    @ApiOperation(value = "全部商品", notes = "全部商品")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "全部商品列表第几页", required = true, example = "1"),
//            @ApiImplicitParam(name = "size", value = "全部商品列表每页多少条数据", required = true, example = "10"),
//            @ApiImplicitParam(name = "sort", value = "排序方式", example = "1"),
//            @ApiImplicitParam(name = "priceGt", value = "价格区间开始", example = "1"),
//            @ApiImplicitParam(name = "priceLte", value = "价格区间结束", example = "1")
//    })
    public ResultT<HomeGoodsResultAdapterRespDTO> homeAllGoods(@RequestParam("page") Integer page,
                                                               @RequestParam("size") Integer size,
                                                               @RequestParam(value = "sort", required = false) Integer sort,
                                                               @RequestParam(value = "priceGt", required = false) Integer priceGt,
                                                               @RequestParam(value = "priceLte", required = false) Integer priceLte) {
        return ResultT.success(homeService.allGoods(page, size, sort, priceGt, priceLte));
    }

}
