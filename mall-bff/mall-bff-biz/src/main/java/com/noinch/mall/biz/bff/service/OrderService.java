package com.noinch.mall.biz.bff.service;

import com.noinch.mall.biz.bff.dto.req.adapter.OrderCreateAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderResultAdapterRespDTO;

public interface OrderService {

    /**
     * 订单列表查看
     *
     * @param page   当前页
     * @param size   每页多少条
     * @param userId 用户 ID
     * @return 订单列表返回数据
     */
    OrderResultAdapterRespDTO listOrder(Integer page, Integer size, String userId);

    /**
     * 根据订单号查询订单详细记录
     *
     * @param orderSn 订单号
     * @return 订单详细返回记录
     */
    OrderAdapterRespDTO getOrderDetail(String orderSn);

    /**
     * 订单创建
     *
     * @param requestParam 订单创建请求参数
     * @return 订单号
     */
    String addOrder(OrderCreateAdapterReqDTO requestParam);

    /**
     * 根据订单号删除订单
     *
     * @param orderSn 订单号
     * @return 是否删除成功
     */
    Integer deleteOrder(String orderSn);
}
