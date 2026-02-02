
package com.noinch.mall.biz.order.infrastructure.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.text.StrBuilder;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.noinch.mall.springboot.starter.mybatisplus.PageUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.order.domain.aggregate.CneeInfo;
import com.noinch.mall.biz.order.domain.aggregate.Order;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.common.OrderCancelErrorCodeEnum;
import com.noinch.mall.biz.order.domain.common.OrderStatusEnum;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import com.noinch.mall.biz.order.infrastructure.dao.entity.OrderDO;
import com.noinch.mall.biz.order.infrastructure.dao.entity.OrderItemDO;
import com.noinch.mall.biz.order.infrastructure.dao.mapper.OrderItemMapper;
import com.noinch.mall.biz.order.infrastructure.dao.mapper.OrderMapper;
import com.noinch.mall.springboot.starter.convention.exception.ClientException;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import com.noinch.mall.springboot.starter.convention.page.PageRequest;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.distributedid.SnowflakeIdUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单仓储层
 *
 */
@Slf4j
@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RedissonClient redissonClient;
    
    @Override
    public Order findOrderByOrderSn(String orderSn) {
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getOrderSn, orderSn);
        OrderDO orderDO = orderMapper.selectOne(queryWrapper);
        Order resultOrder = new Order();
        BeanUtil.copyProperties(orderDO, resultOrder);
        CneeInfo cneeInfo = new CneeInfo();
        BeanUtil.copyProperties(orderDO, cneeInfo);
        resultOrder.setCneeInfo(cneeInfo);
        LambdaQueryWrapper<OrderItemDO> lambdaQueryWrapper = Wrappers.lambdaQuery(OrderItemDO.class)
                .eq(OrderItemDO::getOrderSn, resultOrder.getOrderSn());
        List<OrderItemDO> orderItemList = orderItemMapper.selectList(lambdaQueryWrapper);
        resultOrder.setOrderProducts(orderItemList.stream()
                .map(each -> {
                    OrderProduct orderProduct = new OrderProduct();
                    BeanUtil.copyProperties(each, orderProduct);
                    return orderProduct;
                }).collect(Collectors.toList()));
        return resultOrder;
    }
    
    @Override
    public List<Order> findOrderByCustomerUserId(String customerUserId) {
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getCustomerUserId, customerUserId)
                .orderByDesc(OrderDO::getCreateTime);
        List<OrderDO> orderDOList = orderMapper.selectList(queryWrapper);
        List<Order> resultOrder = orderDOList.stream().map(
                each -> {
                    Order order = new Order();
                    BeanUtil.copyProperties(each, order);
                    CneeInfo cneeInfo = new CneeInfo();
                    BeanUtil.copyProperties(each, cneeInfo);
                    order.setCneeInfo(cneeInfo);
                    return order;
                }
        ).collect(Collectors.toList());
        resultOrder.forEach(each -> {
            LambdaQueryWrapper<OrderItemDO> lambdaQueryWrapper = Wrappers.lambdaQuery(OrderItemDO.class)
                    .eq(OrderItemDO::getOrderSn, each.getOrderSn());
            List<OrderItemDO> orderItemList = orderItemMapper.selectList(lambdaQueryWrapper);
            each.setOrderProducts(orderItemList.stream().map(
                    item -> {
                        OrderProduct orderProduct = new OrderProduct();
                        BeanUtil.copyProperties(item, orderProduct);
                        return orderProduct;
                    }
            ).collect(Collectors.toList()));
        });
        return resultOrder;
    }
    
    @Override
    public void createOrder(Order order) {
        long orderId = SnowflakeIdUtil.nextId();
        OrderDO orderDO = new OrderDO();
        BeanUtil.copyProperties(order, orderDO);
        orderDO.setId(orderId);
        BeanUtil.copyProperties(order.getCneeInfo(), orderDO, CopyOptions.create().ignoreNullValue());
        orderMapper.insert(orderDO);

        List<OrderItemDO> orderItemDOList = order.getOrderProducts().stream().map(
                each -> {
                    OrderItemDO orderItemDO = new OrderItemDO();
                    BeanUtil.copyProperties(each, orderItemDO);
                    orderItemDO.setOrderId(orderId);
                    orderItemDO.setCustomerUserId(order.getCustomerUserId());
                    return orderItemDO;
                }
        ).toList();
        orderItemDOList.forEach(orderItemMapper::insert);
    }
    
    @Override
    public void closeOrder(String orderSn) {
        OrderDO updateOrderDO = new OrderDO();
        updateOrderDO.setStatus(OrderStatusEnum.CLOSED.getStatus());
        LambdaUpdateWrapper<OrderDO> updateWrapper = Wrappers.lambdaUpdate(OrderDO.class)
                .eq(OrderDO::getOrderSn, orderSn);
        orderMapper.update(updateOrderDO, updateWrapper);
    }
    
    @Override
    public void cancelOrder(String orderSn) {
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getOrderSn, orderSn);
        OrderDO orderDO = orderMapper.selectOne(queryWrapper);
        if (orderDO == null) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_UNKNOWN_ERROR);
        } else if (orderDO.getStatus() != OrderStatusEnum.PENDING_PAYMENT.getStatus()) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_STATUS_ERROR);
        }

        RLock lock = redissonClient.getLock(StrBuilder.create("order:cancel:order_sn_").append(orderSn).toString());
        if (!lock.tryLock()) {
            throw new ClientException(OrderCancelErrorCodeEnum.ORDER_CANCEL_REPETITION_ERROR);
        }
        try {
            OrderDO updateOrderDO = new OrderDO();
            updateOrderDO.setStatus(OrderStatusEnum.CLOSED.getStatus());
            updateOrderDO.setOrderSn(orderSn);
            LambdaUpdateWrapper<OrderDO> updateWrapper = Wrappers.lambdaUpdate(OrderDO.class)
                    .eq(OrderDO::getOrderSn, orderSn);
            int updateResult = orderMapper.update(updateOrderDO, updateWrapper);
            if (updateResult <= 0) {
                throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_ERROR);
            }
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void statusReversal(Order order) {
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getOrderSn, order.getOrderSn());
        OrderDO orderDO = orderMapper.selectOne(queryWrapper);
        if (orderDO == null) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_UNKNOWN_ERROR);
        } else if (orderDO.getStatus() != OrderStatusEnum.PENDING_PAYMENT.getStatus()) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_STATUS_ERROR);
        }
        RLock lock = redissonClient.getLock(StrBuilder.create("order:status-reversal:order_sn_").append(order.getOrderSn()).toString());
        if (!lock.tryLock()) {
            log.warn("订单重复修改状态，订单聚合根：{}", JSON.toJSONString(order));
        }
        try {
            OrderDO updateOrderDO = new OrderDO();
            updateOrderDO.setStatus(order.getStatus());
            LambdaUpdateWrapper<OrderDO> updateWrapper = Wrappers.lambdaUpdate(OrderDO.class)
                    .eq(OrderDO::getOrderSn, order.getOrderSn());
            int updateResult = orderMapper.update(updateOrderDO, updateWrapper);
            if (updateResult <= 0) {
                throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_STATUS_REVERSAL_ERROR);
            }
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void deleteOrder(String orderSn) {
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getOrderSn, orderSn);
        OrderDO orderDO = orderMapper.selectOne(queryWrapper);
        if (orderDO == null) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_UNKNOWN_ERROR);
        }
        LambdaUpdateWrapper<OrderDO> updateWrapper = Wrappers.lambdaUpdate(OrderDO.class)
                .eq(OrderDO::getOrderSn, orderSn);
        int updateResult = orderMapper.delete(updateWrapper);
        if (updateResult <= 0) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_DELETE_ERROR);
        }
    }
    
    @Override
    public PageResponse<Order> pageQueryOrder(String userId, PageRequest pageRequest) {
        LambdaQueryWrapper<OrderDO> queryWrapper = Wrappers.lambdaQuery(OrderDO.class).eq(OrderDO::getCustomerUserId, userId);
        Page<OrderDO> orderDOPage = orderMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        PageResponse<Order> actualResult = PageUtil.convert(orderDOPage, Order.class);
        actualResult.convert(each -> {
            LambdaQueryWrapper<OrderItemDO> lambdaQueryWrapper = Wrappers.lambdaQuery(OrderItemDO.class)
                    .eq(OrderItemDO::getOrderSn, each.getOrderSn());
            List<OrderItemDO> orderItemList = orderItemMapper.selectList(lambdaQueryWrapper);
            List<OrderProduct> orderProductList = orderItemList.stream().map(item -> {
                OrderProduct orderProduct = new OrderProduct();
                BeanUtil.copyProperties(item, orderProduct);
                return orderProduct;
            }).toList();
            each.setOrderProducts(orderProductList);
            return each;
        });
        return actualResult;
    }
}
