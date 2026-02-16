

package com.noinch.mall.biz.order.infrastructure.remote;

import com.noinch.mall.biz.order.domain.dto.ProductLockStockReqDTO;
import com.noinch.mall.biz.order.domain.dto.ProductUnlockStockReqDTO;
import com.noinch.mall.biz.order.domain.dto.ProductVerifyStockReqDTO;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 商品库存服务远程调用
 *
 */
@FeignClient(value = "product-service")
//@FeignClient(value = "product-service", url = "${mall.remote-url.product-service")
public interface ProductStockRemoteClient {
    
    /**
     * 验证商品库存
     */
    @PostMapping("/api/product/stock/verify")
    Result<Boolean> verifyProductStock(@RequestBody List<ProductVerifyStockReqDTO> requestParams);
    
    /**
     * 锁定商品库存
     */
    @PutMapping("/api/product/stock/lock")
    Result<Boolean> lockProductStock(@RequestBody ProductLockStockReqDTO requestParam);
    
    /**
     * 解锁商品库存
     */
    @PutMapping("/api/product/stock/unlock")
    Result<Boolean> unlockProductStock(@RequestBody ProductUnlockStockReqDTO requestParam);
}
