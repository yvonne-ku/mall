package com.noinch.mall.biz.order.infrastructure.remote.service;

import cn.hutool.core.bean.BeanUtil;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.service.CartRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.CartRemoteClient;
import com.noinch.mall.biz.order.infrastructure.remote.dto.CartItemQuerySelectRespDTO;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CartRemoteServiceImpl implements CartRemoteService {

    private final CartRemoteClient cartRemoteClient;

    @Override
    public List<OrderProduct> querySelectCartItemByCustomerUserId(String customerUserId) {
        List<OrderProduct> orderProducts = null;
        Result<List<CartItemQuerySelectRespDTO>> res = cartRemoteClient.querySelectCartByCustomerUserId(customerUserId);
        if (res.isSuccess()) {
            orderProducts = res.getData().stream().map(each -> {
                OrderProduct orderProduct = new OrderProduct();
                BeanUtil.copyProperties(each, orderProduct);
                return orderProduct;
            }).toList();
        }
        return orderProducts;
    }
}
