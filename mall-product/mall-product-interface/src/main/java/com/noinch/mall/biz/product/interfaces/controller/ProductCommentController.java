
package com.noinch.mall.biz.product.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.product.application.req.ProductCommentAppendCommand;
import com.noinch.mall.biz.product.application.req.ProductCommentRemoveCommand;
import com.noinch.mall.biz.product.application.req.ProductCommentSaveCommand;
import com.noinch.mall.biz.product.application.service.ProductCommentService;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.log.annotation.MLog;
import com.noinch.mall.springboot.starter.web.Results;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品评论控制层
 */
@MLog
@RestController
@RequiredArgsConstructor
@Tag(name = "商品评论")
public class ProductCommentController {
    
    private final ProductCommentService productCommentService;
    
    @PostMapping("/api/product/comment")
    @Operation(summary = "新增商品评论")
    public Result<Void> saveProductComment(@RequestBody ProductCommentSaveCommand requestParam) {
        productCommentService.saveProductComment(requestParam);
        return Results.success();
    }
    
    @DeleteMapping("/api/product/comment")
    @Operation(summary = "删除商品评论")
    public Result<Void> removeProductComment(@RequestBody ProductCommentRemoveCommand requestParam) {
        productCommentService.removeProductComment(requestParam);
        return Results.success();
    }
    
    @PostMapping("/api/product/comment/append")
    @Operation(summary = "追加商品评论")
    public Result<Void> appendProductComment(@RequestBody ProductCommentAppendCommand requestParam) {
        productCommentService.appendProductComment(requestParam);
        return Results.success();
    }
}
