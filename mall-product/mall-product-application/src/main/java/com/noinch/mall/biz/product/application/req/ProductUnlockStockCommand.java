

package com.noinch.mall.biz.product.application.req;

import lombok.Data;
import com.noinch.mall.biz.product.domain.dto.ProductStockDetailDTO;

import java.util.List;

/**
 * 解锁商品库存
 * */
@Data
public class ProductUnlockStockCommand {
    
    /**
     * 订单号
     */
    private String orderSn;
    
    /**
     * 订单商品详情
     */
    private List<ProductStockDetailDTO> productStockDetails;
}
