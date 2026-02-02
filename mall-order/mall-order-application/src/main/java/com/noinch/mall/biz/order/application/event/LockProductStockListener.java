
package com.noinch.mall.biz.order.application.event;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.order.infrastructure.remote.ProductStockRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.dto.ProductLockStockReqDTO;
import com.noinch.mall.biz.order.infrastructure.remote.dto.ProductStockDetailReqDTO;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 锁定商品库存监听
 *
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public final class LockProductStockListener implements ApplicationListener<OrderCreateEvent> {
    
    private final ProductStockRemoteService productStockRemoteService;
    
    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        ProductLockStockReqDTO requestParam = ProductLockStockReqDTO.builder()
                .orderSn(event.getOrder().getOrderSn())
                .productStockDetails(event.getOrder().getOrderProducts().stream().map(each -> {
                    ProductStockDetailReqDTO productStockDetailReqDTO = new ProductStockDetailReqDTO();
                    BeanUtil.copyProperties(each, productStockDetailReqDTO);
                    return productStockDetailReqDTO;
                }).toList())
                .build();
        try {
            Result<Boolean> lockProductStockResult = productStockRemoteService.lockProductStock(requestParam);
            if (!lockProductStockResult.isSuccess() || !lockProductStockResult.getData()) {
                throw new ServiceException(lockProductStockResult.getMessage());
            }
        } catch (Throwable ex) {
            log.error("锁定商品库存失败, 入参: {}", JSON.toJSONString(requestParam), ex);
            throw ex;
        }
    }
}
