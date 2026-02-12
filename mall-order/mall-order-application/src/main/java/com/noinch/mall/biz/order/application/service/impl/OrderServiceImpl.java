

package com.noinch.mall.biz.order.application.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.noinch.mall.biz.order.domain.event.OrderCreateEvent;
import com.noinch.mall.biz.order.domain.service.CartRemoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.order.application.enums.OrderChainMarkEnum;
import com.noinch.mall.biz.order.application.req.OrderCreateCommand;
import com.noinch.mall.biz.order.application.req.OrderPageQuery;
import com.noinch.mall.biz.order.application.resp.OrderRespDTO;
import com.noinch.mall.biz.order.application.service.OrderService;
import com.noinch.mall.biz.order.domain.aggregate.CneeInfo;
import com.noinch.mall.biz.order.domain.aggregate.Order;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.common.OrderStatusEnum;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import com.noinch.mall.springboot.starter.base.ApplicationContextHolder;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.designpattern.chain.AbstractChainContext;
import com.noinch.mall.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单接口
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRemoteService cartRemoteService;
    private final OrderRepository orderRepository;
    private final AbstractChainContext<OrderCreateCommand> abstractChainContext;
    
    @Transactional
    @Override
    public String createOrder(OrderCreateCommand requestParam) {
        // 责任链模式: 执行订单创建参数验证
        abstractChainContext.handler(OrderChainMarkEnum.ORDER_CREATE_FILTER.name(), requestParam);
        // 创建订单号
        String orderSn = SnowflakeIdUtil.nextIdStrByService(requestParam.getCustomerUserId());
        // 调用购物车服务获取已选中参与结算商品
        List<OrderProduct> orderProducts = cartRemoteService.querySelectCartItemByCustomerUserId(requestParam.getCustomerUserId());
        // 构建订单聚合根
        CneeInfo cneeInfo = new CneeInfo();
        BeanUtil.copyProperties(requestParam, cneeInfo, CopyOptions.create().setIgnoreNullValue(true));
        Order order = Order.builder()
                .customerUserId(Long.parseLong(requestParam.getCustomerUserId()))
                .orderSn(orderSn)
                .totalAmount(requestParam.getTotalAmount())
                .payAmount(requestParam.getPayAmount())
                .freightAmount(requestParam.getFreightAmount())
                .source(requestParam.getSource())
                .type(requestParam.getType())
                .cneeInfo(cneeInfo)
                .remark(requestParam.getRemark())
                .status(OrderStatusEnum.PENDING_PAYMENT.getStatus())
                .orderProducts(orderProducts)
                .build();
        // 观察者模式: 发布商品下单事件
        ApplicationContextHolder.getInstance().publishEvent(new OrderCreateEvent(this, order));
        return orderSn;
    }
    
    @Override
    public OrderRespDTO getOrderByOrderSn(String orderSn) {
        Order order = orderRepository.findOrderByOrderSn(orderSn);
        CneeInfo cneeInfo = order.getCneeInfo();
        OrderRespDTO result = new OrderRespDTO();
        BeanUtil.copyProperties(order, result);
        BeanUtil.copyProperties(cneeInfo, result, CopyOptions.create().setIgnoreNullValue(true));
        return result;
    }
    
    @Override
    public List<OrderRespDTO> getOrderByCustomerUserId(String customerUserId) {
        List<Order> orderList = orderRepository.findOrderByCustomerUserId(customerUserId);
        return orderList.stream()
                .map(each -> {
                    OrderRespDTO result = new OrderRespDTO();
                    BeanUtil.copyProperties(each, result);
                    BeanUtil.copyProperties(each.getCneeInfo(), result, CopyOptions.create().setIgnoreNullValue(true));
                    return result;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public void cancelOrder(String orderSn) {
        orderRepository.cancelOrder(orderSn);
    }
    
    @Override
    public void deleteOrder(String orderSn) {
        orderRepository.deleteOrder(orderSn);
    }
    
    @Override
    public PageResponse<OrderRespDTO> pageQueryOrder(OrderPageQuery requestParam) {
        PageResponse<Order> pageResponse = orderRepository.pageQueryOrder(requestParam.getUserId(), requestParam);
        return pageResponse.convert(each -> {
            OrderRespDTO result = new OrderRespDTO();
            BeanUtil.copyProperties(each, result);
            BeanUtil.copyProperties(each.getCneeInfo(), result, CopyOptions.create().setIgnoreNullValue(true));
            return result;
        });
    }
}
