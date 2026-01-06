package com.noinch.mall.biz.product.interfaces.controller;

import com.noinch.mall.biz.product.application.resp.QuickSearchRespDTO;
import com.noinch.mall.biz.product.domain.mode.ProductIndex;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品服务
 */
@MLog
@RestController
@AllArgsConstructor
@Tag(name = "商品服务")
public class ProductController {
    
    private final ProductService productService;

    @GetMapping("/api/product/page")
    @Operation(summary = "商品分页查询", description = "根据分页查询商品信息")
    public Result<PageResponse<ProductRespDTO>> pageQueryProduct(ProductPageQuery requestParam) {
        return Results.success(productService.pageQueryProduct(requestParam));
    }

    @GetMapping("/api/product/spu/{spuId}")
    @Operation(summary = "查询商品详情", description = "根据 spuId 查询商品详情")
    @Parameter(
        name = "spuId",
        description = "商品 spuId",
        required = true,
        example = "1477055850256982016"
    )
    public Result<ProductRespDTO> getProductBySpuId(@PathVariable("spuId") String spuId) {
        ProductRespDTO result = productService.getProductBySpuId(Long.parseLong(spuId));
        return Results.success(result);
    }

    @GetMapping("/api/product/search")
    @Operation(description = "搜索商品")
    @Parameter(
        name = "description",
        description = "商品描述",
        required = true,
        example = "小米13"
    )
    public Result<List<ProductRespDTO>> searchProduct(@RequestParam(value = "description") String description,
                                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return Results.success(productService.searchProduct(description, page, size));
    }
}
