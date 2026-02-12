package com.noinch.mall.biz.order.domain.service;

import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;

import java.util.List;

public interface CartRemoteService {

    List<OrderProduct> querySelectCartItemByCustomerUserId(String customerUserId);


}
