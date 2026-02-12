package com.noinch.mall.biz.bff.service.impl;

import com.noinch.mall.biz.bff.dto.req.adapter.OrderCreateAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderAddressAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderGoodsAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.OrderResultAdapterRespDTO;
import com.noinch.mall.biz.bff.remote.CartRemoteClient;
import com.noinch.mall.biz.bff.remote.OrderRemoteClient;
import com.noinch.mall.biz.bff.remote.req.OrderCreateCommand;
import com.noinch.mall.biz.bff.remote.resp.CartItemQuerySelectRespDTO;
import com.noinch.mall.biz.bff.remote.resp.OrderProductRespDTO;
import com.noinch.mall.biz.bff.remote.resp.OrderRespDTO;
import com.noinch.mall.biz.bff.service.OrderService;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service("bffOrderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRemoteClient orderRemoteClient;
    private final CartRemoteClient cartRemoteClient;

    @Override
    public OrderResultAdapterRespDTO listOrder(Integer page, Integer size, String userId) {
        Result<PageResponse<OrderRespDTO>> orderListRemoteResult;
        PageResponse<OrderRespDTO> orderPages;

        // 调用订单服务查询订单
        try {
            orderListRemoteResult = orderRemoteClient.pageQueryOrder(userId, page, size);
            if (!orderListRemoteResult.isSuccess() || orderListRemoteResult.getData() == null) {
                throw new ServiceException("调用订单服务查询订单失败");
            }
            orderPages = orderListRemoteResult.getData();
        } catch (Throwable ex) {
            log.error("调用订单服务查询订单失败", ex);
            throw ex;
        }

        // 构造前端返回
        List<OrderAdapterRespDTO> orderListResult = new ArrayList<>();
        for (OrderRespDTO each : orderPages.getRecords()) {
            OrderAdapterRespDTO orderAdapter = new OrderAdapterRespDTO();
            orderAdapter.setOrderId(each.getOrderSn());
            orderAdapter.setOrderTotal(each.getTotalAmount().intValue());
            orderAdapter.setOrderStatus(String.valueOf(each.getStatus()));
            orderAdapter.setFinishDate(each.getReceiveTime());
            orderAdapter.setCreateDate(each.getCreateTime());
            OrderAddressAdapterRespDTO addressInfo = new OrderAddressAdapterRespDTO();
            addressInfo.setTel(each.getCneePhone());
            addressInfo.setStreetName(each.getCneeDetailAddress());
            addressInfo.setUserName(each.getCneeName());
            orderAdapter.setAddressInfo(addressInfo);
            List<OrderGoodsAdapterRespDTO> goodsList = new ArrayList<>();
            for (OrderProductRespDTO item : each.getOrderProducts()) {
                OrderGoodsAdapterRespDTO goods = new OrderGoodsAdapterRespDTO();
                goods.setProductId(item.getProductId());
                goods.setProductImg(item.getProductPic());
                goods.setProductName(item.getProductName());
                goods.setProductNum(item.getProductQuantity());
                goods.setSalePrice(item.getProductPrice().intValue());
                goodsList.add(goods);
            }
            orderAdapter.setGoodsList(goodsList);
            orderListResult.add(orderAdapter);
        }
        return new OrderResultAdapterRespDTO(orderPages.getTotal().intValue(), orderListResult);
    }

    @Override
//    @Cached(name = "order:", key = "'query-order-'+#orderSn", expire = 24, timeUnit = TimeUnit.HOURS)
    public OrderAdapterRespDTO getOrderDetail(String orderSn) {
        Result<OrderRespDTO> orderDetailRemoteResult;
        OrderRespDTO orderRespDTO;

        // 调用订单服务查询订单详情
        try {
            orderDetailRemoteResult = orderRemoteClient.getOrderByOrderSn(orderSn);
            if (!orderDetailRemoteResult.isSuccess() || orderDetailRemoteResult.getData() == null) {
                throw new ServiceException("调用订单服务查询详情失败");
            }
            orderRespDTO = orderDetailRemoteResult.getData();
        } catch (Throwable ex) {
            log.error("调用订单服务查询详情失败", ex);
            throw ex;
        }

        // 构造前端返回
        OrderAdapterRespDTO orderListResult = new OrderAdapterRespDTO();
        orderListResult.setOrderId(orderRespDTO.getOrderSn());
        orderListResult.setOrderTotal(orderRespDTO.getTotalAmount().intValue());
        orderListResult.setOrderStatus(String.valueOf(orderRespDTO.getStatus()));
        orderListResult.setFinishDate(orderRespDTO.getReceiveTime());
        orderListResult.setCreateDate(orderRespDTO.getCreateTime());
        OrderAddressAdapterRespDTO addressInfo = new OrderAddressAdapterRespDTO();
        addressInfo.setTel(orderRespDTO.getCneePhone());
        addressInfo.setStreetName(orderRespDTO.getCneeDetailAddress());
        addressInfo.setUserName(orderRespDTO.getCneeName());
        orderListResult.setAddressInfo(addressInfo);
        List<OrderGoodsAdapterRespDTO> goodsList = new ArrayList<>();
        for (OrderProductRespDTO item : orderRespDTO.getOrderProducts()) {
            OrderGoodsAdapterRespDTO goods = new OrderGoodsAdapterRespDTO();
            goods.setProductId(item.getProductId());
            goods.setProductImg(item.getProductPic());
            goods.setProductName(item.getProductName());
            goods.setProductNum(item.getProductQuantity());
            goods.setSalePrice(item.getProductPrice().intValue());
            goodsList.add(goods);
        }
        orderListResult.setGoodsList(goodsList);
        return orderListResult;
    }

    @Override
    public String addOrder(OrderCreateAdapterReqDTO requestParam) {
        List<CartItemQuerySelectRespDTO> userCartList;
        Result<List<CartItemQuerySelectRespDTO>> selectCartListResult;

        // 查询用户选中商品
        try {
            selectCartListResult = cartRemoteClient.querySelectCartItemByCustomerUserId(requestParam.getUserId());
            if (!selectCartListResult.isSuccess() || selectCartListResult.getData() == null) {
                throw new ServiceException("调用购物车服务查询用户选中商品失败");
            }
            userCartList = selectCartListResult.getData();
        } catch (Throwable ex) {
            log.error("调用购物车服务查询用户选中商品失败", ex);
            throw ex;
        }

        // 计算订单金额
        BigDecimal amount = new BigDecimal("0");
        for (CartItemQuerySelectRespDTO each : userCartList) {
            if (each.getProductPrice() != null) {
                amount = amount.add(each.getProductPrice().multiply(BigDecimal.valueOf(each.getProductQuantity())));
            }
        }

        // 创建订单 req
        OrderCreateCommand orderCreateRemoteRequestParam = new OrderCreateCommand();
        orderCreateRemoteRequestParam.setCustomerUserId(requestParam.getUserId());
        orderCreateRemoteRequestParam.setTotalAmount(amount);
        orderCreateRemoteRequestParam.setPayAmount(amount);
        orderCreateRemoteRequestParam.setFreightAmount(new BigDecimal("0"));
        orderCreateRemoteRequestParam.setSource(0);
        orderCreateRemoteRequestParam.setType(0);
        orderCreateRemoteRequestParam.setCneeName(requestParam.getUserName());
        orderCreateRemoteRequestParam.setCneePhone(requestParam.getTel());
        orderCreateRemoteRequestParam.setCneeDetailAddress(requestParam.getStreetName());
        Result<String> orderCreateRemoteResult;
        try {
            orderCreateRemoteResult = orderRemoteClient.createOrder(orderCreateRemoteRequestParam);
            if (!orderCreateRemoteResult.isSuccess() || orderCreateRemoteResult.getData() == null) {
                throw new ServiceException("调用订单服务创建订单失败");
            }
        } catch (Throwable ex) {
            log.error("调用订单服务创建订单失败", ex);
            throw ex;
        }
        return orderCreateRemoteResult.getData();
    }

    @Override
//    @CacheInvalidate(name = "order:", key = "'query-order-'+#orderSn")
    public Integer deleteOrder(String orderSn) {
        int deleteOrderFlag = 0;
        try {
            Result<Void> deletedOrderResult = orderRemoteClient.deleteOrder(orderSn);
            if (deletedOrderResult.isSuccess()) {
                deleteOrderFlag = 1;
            }
        } catch (Throwable ex) {
            log.error("调用订单服务删除订单失败", ex);
        }
        return deleteOrderFlag;
    }

    @Override
    public Integer cancelOrder(String orderSn) {
        int cancelOrderFlag = 0;
        try {
            Result<Void> cancelledOrderResult = orderRemoteClient.cancelOrder(orderSn);
            if (cancelledOrderResult.isSuccess()) {
                cancelOrderFlag = 1;
            }
        } catch (Throwable ex) {
            log.error("调用订单服务取消订单失败", ex);
        }
        return cancelOrderFlag;
    }
}
