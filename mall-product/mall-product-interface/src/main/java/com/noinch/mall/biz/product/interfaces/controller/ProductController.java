package com.noinch.mall.biz.product.interfaces.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.product.application.req.ProductPageQuery;
import com.noinch.mall.biz.product.application.resp.ProductRespDTO;
import com.noinch.mall.biz.product.application.service.ProductService;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.log.annotation.MLog;
import com.noinch.mall.springboot.starter.web.Results;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品服务
 * */
@MLog
@RestController
@AllArgsConstructor
@Api(tags = "商品服务")
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping("/api/product/spu/{spuId}")
    @ApiOperation(value = "查询商品详情", notes = "根据 spuId 查询商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spuId", value = "商品 spuId", required = true, example = "1477055850256982016")
    })
    public Result<ProductRespDTO> getProductBySpuId(@PathVariable("spuId") String spuId) {
        ProductRespDTO result = productService.getProductBySpuId(Long.parseLong(spuId));
        return Results.success(result);
    }
    
    @GetMapping("/api/product/page")
    @ApiOperation(value = "商品分页查询", notes = "商品分页查询返回 SPU 信息")
    public Result<PageResponse<ProductRespDTO>> pageQueryProduct(ProductPageQuery requestParam) {
        return Results.success(productService.pageQueryProduct(requestParam));
    }
}
