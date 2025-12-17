

package com.noinch.mall.biz.product.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.noinch.mall.ddd.framework.core.domain.AggregateRoot;

import java.util.List;

/**
 * 商品库存聚合根
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStock implements AggregateRoot {
    
    /**
     * 订单号
     */
    private String orderSn;
    
    /**
     * 商品库存详情
     */
    private List<ProductStockDetail> productStockDetails;
}
