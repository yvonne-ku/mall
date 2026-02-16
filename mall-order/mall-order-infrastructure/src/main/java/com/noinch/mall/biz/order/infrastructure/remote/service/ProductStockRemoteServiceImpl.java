package com.noinch.mall.biz.order.infrastructure.remote.service;

import cn.hutool.core.bean.BeanUtil;
import com.noinch.mall.biz.order.domain.aggregate.OrderProduct;
import com.noinch.mall.biz.order.domain.dto.ProductVerifyStockReqDTO;
import com.noinch.mall.biz.order.domain.event.OrderCreateEvent;
import com.noinch.mall.biz.order.domain.repository.OrderRepository;
import com.noinch.mall.biz.order.domain.service.ProductStockRemoteService;
import com.noinch.mall.biz.order.infrastructure.remote.ProductStockRemoteClient;
import com.noinch.mall.biz.order.domain.dto.ProductLockStockReqDTO;
import com.noinch.mall.biz.order.domain.dto.ProductStockDetailReqDTO;
import com.noinch.mall.biz.order.domain.dto.ProductUnlockStockReqDTO;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProductStockRemoteServiceImpl implements ProductStockRemoteService {

    private final OrderRepository orderRepository;
    private final ProductStockRemoteClient productStockRemoteClient;

    public void unlockProductStock(String orderSn){
        List<OrderProduct> orderItems = orderRepository.findOrderProductByOrderSn(orderSn);
        ProductUnlockStockReqDTO req = new ProductUnlockStockReqDTO();
        req.setOrderSn(orderSn);
        req.setProductStockDetails(orderItems.stream().map(item ->
                ProductStockDetailReqDTO.builder()
                        .productId(String.valueOf(item.getProductId()))
                        .productSkuId(String.valueOf(item.getProductSkuId()))
                        .productQuantity(item.getProductQuantity())
                        .build()
        ).collect(Collectors.toList()));
        Result<Boolean> result = productStockRemoteClient.unlockProductStock(req);
        if (! result.isSuccess()) {
            log.error("调用远程商品库存服务，解锁库存失败。");
            throw new ServiceException(BaseErrorCode.SERVICE_REMOTE_ERROR);
        }
    }

    @Override
    public void lockProductStock(OrderCreateEvent event) {
        ProductLockStockReqDTO req = ProductLockStockReqDTO.builder()
                .orderSn(event.getOrder().getOrderSn())
                .productStockDetails(event.getOrder().getOrderProducts().stream().map(each -> {
                    ProductStockDetailReqDTO productStockDetailReqDTO = new ProductStockDetailReqDTO();
                    BeanUtil.copyProperties(each, productStockDetailReqDTO);
                    return productStockDetailReqDTO;
                }).toList())
                .build();
        Result<Boolean> result = productStockRemoteClient.lockProductStock(req);
        if (! result.isSuccess()) {
            log.error("调用远程商品库存服务，锁定库存失败。");
            throw new ServiceException(BaseErrorCode.SERVICE_REMOTE_ERROR);
        }
    }

    @Override
    public Boolean verifyProductStock(List<ProductVerifyStockReqDTO> requestParam) {
        Result<Boolean> res = productStockRemoteClient.verifyProductStock(requestParam);
        if (! res.isSuccess()) {
            log.error("调用远程商品库存服务，验证库存失败。");
            throw new ServiceException(BaseErrorCode.SERVICE_REMOTE_ERROR);
        }
        return true;
    }
}
