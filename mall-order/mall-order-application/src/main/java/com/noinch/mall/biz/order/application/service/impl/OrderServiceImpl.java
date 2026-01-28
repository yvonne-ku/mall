

package com.noinch.mall.biz.order.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import com.noinch.mall.biz.order.application.enums.OrderChainMarkEnum;
import com.noinch.mall.biz.order.application.event.order.create.OrderCreateEvent;
import com.noinch.mall.biz.order.application.filter.OrderCreateProductSkuStockChainHandler;
import com.noinch.mall.biz.order.application.req.OrderCreateCommand;
import com.noinch.mall.biz.order.application.req.OrderPageQuery;
import com.noinch.mall.biz.order.application.resp.OrderRespDTO;
import com.noinch.mall.biz.order.application.service.OrderService;
import com.noinch.mall.biz.order.domain.aggregate.CneeInfo;
import com.noinch.mall.biz.order.domain.aggregate.Order;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.common.OrderStatusEnum;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import com.noinch.mall.biz.order.infrastructure.remote.CartRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.dto.CartItemQuerySelectRespDTO;
import com.noinch.mall.springboot.starter.base.ApplicationContextHolder;
import com.noinch.mall.springboot.starter.common.toolkit.BeanUtil;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.convention.result.Result;
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
    @ShardingSphereTransactionType(TransactionType.BASE)
    @Override
    public String createOrder(OrderCreateCommand requestParam) {
        // 责任链模式: 执行订单创建参数验证
        abstractChainContext.handler(OrderChainMarkEnum.ORDER_CREATE_FILTER.name(), requestParam);
        // 创建订单号
        String orderSn = SnowflakeIdUtil.nextIdStrByService(requestParam.getCustomerUserId());
        // 调用购物车服务获取已选中参与结算商品
        List<CartItemQuerySelectRespDTO> cartProducts = querySelectCartByCustomerUserId(requestParam.getCustomerUserId());
        List<OrderProduct> orderProducts = cartProducts.stream()
                .map(each -> BeanUtil.convert(each, OrderProduct.class).setOrderSn(orderSn))
                .collect(Collectors.toList());
        // 构建订单聚合根
        Order order = Order.builder()
                .customerUserId(Long.parseLong(requestParam.getCustomerUserId()))
                .orderSn(orderSn)
                .totalAmount(requestParam.getTotalAmount())
                .payAmount(requestParam.getPayAmount())
                .freightAmount(requestParam.getFreightAmount())
                .source(requestParam.getSource())
                .type(requestParam.getType())
                .cneeInfo(BeanUtil.convert(requestParam, CneeInfo.class))
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
        OrderRespDTO result = BeanUtil.convert(order, OrderRespDTO.class);
        BeanUtil.convertIgnoreNullAndBlank(cneeInfo, result);
        return result;
    }
    
    @Override
    public List<OrderRespDTO> getOrderByCustomerUserId(String customerUserId) {
        List<Order> orderList = orderRepository.findOrderByCustomerUserId(customerUserId);
        return BeanUtil.convert(orderList, OrderRespDTO.class);
    }
    
    @Override
    public void canalOrder(String orderSn) {
        orderRepository.canalOrder(orderSn);
    }
    
    @Override
    public void deleteOrder(String orderSn) {
        orderRepository.deleteOrder(orderSn);
    }
    
    @Override
    public PageResponse<OrderRespDTO> pageQueryOrder(OrderPageQuery requestParam) {
        PageResponse<Order> pageResponse = orderRepository.pageQueryOrder(requestParam.getUserId(), requestParam);
        return pageResponse.convert(each -> BeanUtil.convert(each, OrderRespDTO.class));
    }
    
    /**
     * 根据用户ID查询选中状态购物车商品
     * <p>
     * 因为创建订单前 {@link OrderCreateProductSkuStockChainHandler#handler(OrderCreateCommand)} 已经验证过，这里直接获取 Data
     *
     * @param customerUserId 用户ID
     * @return 用户购物车选中商品，参与订单结算
     */
    private List<CartItemQuerySelectRespDTO> querySelectCartByCustomerUserId(String customerUserId) {
        Result<List<CartItemQuerySelectRespDTO>> cartProductsResult = cartRemoteService.querySelectCartByCustomerUserId(customerUserId);
        return cartProductsResult.getData();
    }
}
