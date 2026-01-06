package com.noinch.mall.biz.bff.remote;

import com.noinch.mall.biz.bff.remote.resp.ProductRespDTO;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.convention.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品服务远程调用
 *
 */
@FeignClient(value = "product-service", url = "${mall.product-service.url:}")
public interface ProductRemoteService {

    /**
     * 根据 SpuID 查询商品详情
     */
    @GetMapping("/api/product/spu/{spuId}")
    Result<ProductRespDTO> getProductBySpuId(@PathVariable("spuId") String spuId);

    /**
     * 商品分页查询返回 SPU 信息
     */
    @GetMapping("/api/product/page")
    Result<PageResponse<ProductRespDTO>> pageQueryProduct(@RequestParam("current") Integer page,
                                                          @RequestParam("size") Integer size,
                                                          @RequestParam(value = "sort", required = false) Integer sort,
                                                          @RequestParam(value = "priceGt", required = false) Integer priceGt,
                                                          @RequestParam(value = "priceLte", required = false) Integer priceLte);

    @GetMapping("/api/product/search")
    Result<List<ProductRespDTO>> searchProduct(@RequestParam(value = "description") String description,
                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size);
}
