

package com.noinch.mall.biz.order.application.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.dto.CartItemQuerySelectRespDTO;
import com.noinch.mall.biz.order.domain.dto.ProductVerifyStockReqDTO;
import com.noinch.mall.biz.order.domain.service.CartRemoteService;
import com.noinch.mall.biz.order.domain.service.ProductStockRemoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.order.application.filter.base.OrderCreateChainFilter;
import com.noinch.mall.biz.order.application.req.OrderCreateCommand;
import com.noinch.mall.biz.order.domain.common.OrderCreateErrorCodeEnum;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 订单创建商品 SKU 库存验证
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class OrderCreateProductSkuStockChainHandler implements OrderCreateChainFilter<OrderCreateCommand> {
    
    private final CartRemoteService cartRemoteService;
    
    private final ProductStockRemoteService productStockRemoteService;
    
    @Override
    public void handler(OrderCreateCommand requestParam) {
        // 创建订单时购物车不能为空
        List<OrderProduct> cartProductsResult = cartRemoteService.querySelectCartItemByCustomerUserId(requestParam.getCustomerUserId());
        if (cartProductsResult == null || cartProductsResult.isEmpty()) {
            log.error(OrderCreateErrorCodeEnum.PRODUCT_CART_ISNULL_ERROR.message());
            throw new ServiceException(OrderCreateErrorCodeEnum.PRODUCT_CART_ISNULL_ERROR);
        }

        // 创建订单时商品必须有库存
        try {
            Boolean verifyProductStockResult = productStockRemoteService.verifyProductStock(cartProductsResult.stream().map(each -> {
                ProductVerifyStockReqDTO productVerifyStockReqDTO = new ProductVerifyStockReqDTO();
                BeanUtil.copyProperties(each, productVerifyStockReqDTO);
                return productVerifyStockReqDTO;
            }).toList());
        } catch (Throwable ex) {
            log.error(OrderCreateErrorCodeEnum.PRODUCT_STOCK_VERIFY_ERROR.message(), ex);
            throw new ServiceException(OrderCreateErrorCodeEnum.PRODUCT_STOCK_VERIFY_ERROR);
        }
    }
    
    @Override
    public int getOrder() {
        return 2;
    }
}
