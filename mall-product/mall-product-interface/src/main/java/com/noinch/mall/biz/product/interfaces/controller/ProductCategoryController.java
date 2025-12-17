

package com.noinch.mall.biz.product.interfaces.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.product.application.resp.ProductCategoryRespDTO;
import com.noinch.mall.biz.product.application.service.ProductCategoryService;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.log.annotation.MLog;
import com.noinch.mall.springboot.starter.web.Results;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品分类
 * */
@MLog
@RestController
@AllArgsConstructor
@Api(tags = "商品分类")
public class ProductCategoryController {
    
    private final ProductCategoryService productCategoryService;
    
    @GetMapping("/api/product/categories")
    @ApiOperation(value = "查询商品分类集合", notes = "返回全部分类")
    public Result<List<ProductCategoryRespDTO>> listAllProductCategory() {
        return Results.success(productCategoryService.listAllProductCategory());
    }
}
