

package com.noinch.mall.biz.product.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "商品分类")
public class ProductCategoryController {
    
    private final ProductCategoryService productCategoryService;
    
    @GetMapping("/api/product/categories")
    @Operation(summary = "查询商品分类集合")
    public Result<List<ProductCategoryRespDTO>> listAllProductCategory() {
        return Results.success(productCategoryService.listAllProductCategory());
    }
}
