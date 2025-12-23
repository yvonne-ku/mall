package com.noinch.mall.biz.bff.remote;

import com.noinch.mall.biz.bff.remote.resp.ProductRespDTO;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
}
