
package com.noinch.mall.biz.product.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.product.application.req.ProductLockStockCommand;
import com.noinch.mall.biz.product.application.req.ProductStockVerifyQuery;
import com.noinch.mall.biz.product.application.req.ProductUnlockStockCommand;
import com.noinch.mall.biz.product.application.service.ProductService;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.log.annotation.MLog;
import com.noinch.mall.springboot.starter.web.Results;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品库存控制层
 */
@MLog
@RestController
@AllArgsConstructor
@Tag(name = "商品库存")
public class ProductStockController {
    
    private final ProductService productService;
    
    @PostMapping("/api/product/stock/verify")
    @Operation(description = "验证商品库存")
    public Result<Boolean> verifyProductStock(@RequestBody List<ProductStockVerifyQuery> requestParams) {
        boolean result = productService.verifyProductStock(requestParams);
        return Results.success(result);
    }
    
//    @Idempotent(
//            key = "'stock_lock_'+#requestParam.orderSn+'_'+#requestParam.hashCode()",
//            type = IdempotentTypeEnum.SPEL,
//            scene = IdempotentSceneEnum.RESTAPI
//    )
    @PutMapping("/api/product/stock/lock")
    @Operation(description = "锁定商品库存")
    public Result<Boolean> lockProductStock(@RequestBody ProductLockStockCommand requestParam) {
        boolean result = productService.lockProductStock(requestParam);
        return Results.success(result);
    }
    
//    @Idempotent(
//            key = "'stock_unlock_'+#requestParam.orderSn+'_'+#requestParam.hashCode()",
//            type = IdempotentTypeEnum.SPEL,
//            scene = IdempotentSceneEnum.RESTAPI
//    )
    @PutMapping("/api/product/stock/unlock")
    @Operation(description = "解锁商品库存")
    public Result<Boolean> unlockProductStock(@RequestBody ProductUnlockStockCommand requestParam) {
        boolean result = productService.unlockProductStock(requestParam);
        return Results.success(result);
    }
}
