

package com.noinch.mall.biz.order.application.service;

import com.noinch.mall.biz.order.application.req.OrderCreateCommand;
import com.noinch.mall.biz.order.application.req.OrderPageQuery;
import com.noinch.mall.biz.order.application.resp.OrderRespDTO;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;

import java.util.List;

/**
 * 订单接口
 *
 */
public interface OrderService {
    
    /**
     * 创建商品订单
     *
     * @param requestParam 商品订单入参
     * @return 订单号
     */
    String createOrder(OrderCreateCommand requestParam);
    
    /**
     * 查询订单信息
     *
     * @param orderSn 订单号
     * @return 订单基本信息
     */
    OrderRespDTO getOrderByOrderSn(String orderSn);
    
    /**
     * 查询订单信息
     *
     * @param customerUserId 用户 ID
     * @return 用户订单信息集合
     */
    List<OrderRespDTO> getOrderByCustomerUserId(String customerUserId);
    
    /**
     * 取消订单
     *
     * @param orderSn 订单号
     */
    void canalOrder(String orderSn);
    
    /**
     * 删除订单
     *
     * @param orderSn 订单号
     */
    void deleteOrder(String orderSn);
    
    /**
     * 分页查询订单列表
     *
     * @param requestParam 分页查询订单列表入参
     * @return 分页查询订单列表返回数据
     */
    PageResponse<OrderRespDTO> pageQueryOrder(OrderPageQuery requestParam);
}
