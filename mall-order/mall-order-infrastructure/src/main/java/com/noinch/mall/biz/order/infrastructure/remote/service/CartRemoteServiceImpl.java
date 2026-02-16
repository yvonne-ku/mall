package com.noinch.mall.biz.order.infrastructure.remote.service;

import cn.hutool.core.bean.BeanUtil;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.dto.CartItemDelReqDTO;
import com.noinch.mall.biz.order.domain.service.CartRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.CartRemoteClient;
import com.noinch.mall.biz.order.domain.dto.CartItemQuerySelectRespDTO;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CartRemoteServiceImpl implements CartRemoteService {

    private final CartRemoteClient cartRemoteClient;

    @Override
    public List<OrderProduct> querySelectCartItemByCustomerUserId(String customerUserId) {
        List<OrderProduct> orderProducts = null;
        Result<List<CartItemQuerySelectRespDTO>> res = cartRemoteClient.querySelectCartByCustomerUserId(customerUserId);
        if (! res.isSuccess()) {
            log.error("调用远程购物车服务，获得购物车商品失败。");
            throw new ServiceException(BaseErrorCode.SERVICE_REMOTE_ERROR);
        }
        orderProducts = res.getData().stream().map(each -> {
            OrderProduct orderProduct = new OrderProduct();
            BeanUtil.copyProperties(each, orderProduct);
            return orderProduct;
        }).toList();
        return orderProducts;
    }

    @Override
    public void clearCartProduct(CartItemDelReqDTO cartItemDelReqDTO) {
        Result<Void> res = cartRemoteClient.clearCartProduct(cartItemDelReqDTO);
        if (! res.isSuccess()) {
            log.error("调用远程购物车服务，清空购物车商品失败。");
            throw new ServiceException(BaseErrorCode.SERVICE_REMOTE_ERROR);
        }
    }
}
