

package com.noinch.mall.biz.order.application.event;

import com.noinch.mall.biz.order.domain.dto.CartItemDelReqDTO;
import com.noinch.mall.biz.order.domain.event.OrderCreateEvent;
import com.noinch.mall.biz.order.domain.service.CartRemoteService;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * 清理购物车商品监听
 *
 */
@Order(1)
@Component
@RequiredArgsConstructor
public final class ClearCartProductListener implements ApplicationListener<OrderCreateEvent> {
    
    private final CartRemoteService cartRemoteService;
    
    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        CartItemDelReqDTO cartItemDelReqDTO = new CartItemDelReqDTO();
        cartItemDelReqDTO.setCustomerUserId(String.valueOf(event.getOrder().getCustomerUserId()));
        cartItemDelReqDTO.setProductSkuIds(event.getOrder().getOrderProducts().stream().map(OrderProduct::getProductSkuId).map(String::valueOf).collect(Collectors.toList()));
        cartRemoteService.clearCartProduct(cartItemDelReqDTO);
    }
}
