package com.noinch.mall.biz.bff.service.impl;

import com.noinch.mall.biz.bff.dto.req.adapter.OrderCreateAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderResultAdapterRespDTO;
import com.noinch.mall.biz.bff.service.OrderService;

public class OrderServiceImpl implements OrderService {

    @Override
    public OrderResultAdapterRespDTO listOrder(Integer page, Integer size, String userId) {
        return null;
    }

    @Override
    public OrderAdapterRespDTO getOrderDetail(String orderSn) {
        return null;
    }

    @Override
    public String addOrder(OrderCreateAdapterReqDTO requestParam) {
        return "";
    }

    @Override
    public Integer deleteOrder(String orderSn) {
        return 0;
    }
}
