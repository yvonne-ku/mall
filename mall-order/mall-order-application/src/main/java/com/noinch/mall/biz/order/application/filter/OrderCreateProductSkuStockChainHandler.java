

package com.noinch.mall.biz.order.application.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.order.application.filter.base.OrderCreateChainFilter;
import com.noinch.mall.biz.order.application.req.OrderCreateCommand;
import com.noinch.mall.biz.order.domain.common.OrderCreateErrorCodeEnum;
import com.noinch.mall.biz.order.infrastructure.remote.CartRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.ProductStockRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.dto.CartItemQuerySelectRespDTO;
import com.noinch.mall.biz.order.infrastructure.remote.dto.ProductVerifyStockReqDTO;
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
        List<CartItemQuerySelectRespDTO> actualResultData;
        try {
            Result<List<CartItemQuerySelectRespDTO>> cartProductsResult = cartRemoteService.querySelectCartByCustomerUserId(requestParam.getCustomerUserId());
            actualResultData = Optional.ofNullable(cartProductsResult)
                    .filter(each -> each.isSuccess())
                    .filter(each -> CollUtil.isNotEmpty(each.getData()))
                    .map(each -> each.getData())
                    .orElseThrow(() -> new ServiceException(OrderCreateErrorCodeEnum.PRODUCT_CART_ISNULL_ERROR));
        } catch (Throwable ex) {
            log.error(OrderCreateErrorCodeEnum.PRODUCT_CART_ISNULL_ERROR.message(), ex);
            throw new ServiceException(OrderCreateErrorCodeEnum.PRODUCT_CART_ISNULL_ERROR);
        }
        try {

            Result<Boolean> verifyProductStockResult = productStockRemoteService.verifyProductStock(actualResultData.stream().map(each -> {
                ProductVerifyStockReqDTO productVerifyStockReqDTO = new ProductVerifyStockReqDTO();
                BeanUtil.copyProperties(each, productVerifyStockReqDTO);
                return productVerifyStockReqDTO;
            }).toList());
            Optional.ofNullable(verifyProductStockResult)
                    .filter(each -> each.isSuccess())
                    .filter(each -> each.getData() != null && each.getData())
                    .orElseThrow(() -> new ServiceException(OrderCreateErrorCodeEnum.PRODUCT_STOCK_VERIFY_ERROR));
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
