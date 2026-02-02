
package com.noinch.mall.biz.order.domain.event;

import com.noinch.mall.biz.order.domain.dto.ProductSkuStockDTO;
import com.noinch.mall.ddd.framework.core.domain.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

/**
 * 延迟关闭订单事件
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayCloseOrderEvent implements DomainEvent {
    
    /**
     * 订单号
     */
    private String orderSn;
    
    /**
     * 参与订单的商品 SKU 以及数量，用于回退库存
     */
    private List<ProductSkuStockDTO> productSkuStockList;
}
