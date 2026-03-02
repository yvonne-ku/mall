package com.noinch.mall.biz.order.infrastructure.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import com.noinch.mall.biz.order.infrastructure.dao.entity.OrderItemDO;
import com.noinch.mall.biz.order.infrastructure.dao.mapper.OrderItemMapper;
import com.noinch.mall.biz.order.infrastructure.remote.ProductStockRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.dto.ProductStockDetailReqDTO;
import com.noinch.mall.biz.order.infrastructure.remote.dto.ProductUnlockStockReqDTO;
import com.noinch.mall.biz.order.infrastructure.service.OrderInfraService;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OrderInfraServiceImpl implements OrderInfraService {

    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductStockRemoteService productStockRemoteService;

    @Override
    public boolean delayCloseOrder(String orderSn) {
        // 1. 关闭订单
        orderRepository.closeOrder(orderSn);

        // 2. 回滚库存
        ProductUnlockStockReqDTO req = new ProductUnlockStockReqDTO();
        req.setOrderSn(orderSn);
        LambdaQueryWrapper<OrderItemDO> lambdaQueryWrapper = Wrappers.lambdaQuery(OrderItemDO.class)
                .eq(OrderItemDO::getOrderSn, orderSn);
        List<OrderItemDO> orderItems = orderItemMapper.selectList(lambdaQueryWrapper);
        req.setProductStockDetails(orderItems.stream().map(item -> ProductStockDetailReqDTO.builder()
                .productId(String.valueOf(item.getProductId()))
                .productSkuId(String.valueOf(item.getProductSkuId()))
                .productQuantity(item.getProductQuantity())
                .build()).collect(Collectors.toList()));
        Result<Boolean> result = productStockRemoteService.unlockProductStock(req);

        if (!result.isSuccess()) {
            log.error("订单 {} 回滚库存失败: {}", orderSn, result);
            return false;
        }
        return true;
    }
}
