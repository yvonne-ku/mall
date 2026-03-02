
package com.noinch.mall.biz.order.infrastructure.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 解锁商品库存
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnlockStockReqDTO {
    
    /**
     * 订单号
     */
    private String orderSn;
    
    /**
     * 订单商品详情
     */
    private List<ProductStockDetailReqDTO> productStockDetails;
}
