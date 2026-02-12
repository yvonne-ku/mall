package com.noinch.mall.biz.bff.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.noinch.mall.biz.bff.common.SelectFlagEnum;
import com.noinch.mall.biz.bff.dto.req.adapter.*;
import com.noinch.mall.biz.bff.dto.resp.adapter.CartAdapterRespDTO;
import com.noinch.mall.biz.bff.remote.CartRemoteClient;
import com.noinch.mall.biz.bff.remote.ProductRemoteClient;
import com.noinch.mall.biz.bff.remote.req.*;
import com.noinch.mall.biz.bff.remote.resp.CartItemRespDTO;
import com.noinch.mall.biz.bff.remote.resp.ProductRespDTO;
import com.noinch.mall.biz.bff.remote.resp.ProductSkuRespDTO;
import com.noinch.mall.biz.bff.remote.resp.ProductSpuRespDTO;
import com.noinch.mall.biz.bff.service.CartService;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRemoteClient cartRemoteClient;
    private final ProductRemoteClient productRemoteClient;

    private static final long PRODUCT_CART_CURRENT = 1L;
    private static final long PRODUCT_CART_SIZE = 500L;

    @Override
    public List<CartAdapterRespDTO> listAllProductCart(String userId) {
        Result<PageResponse<CartItemRespDTO>> remoteProductResult = null;

        // 调用远程服务获取所当页所有 cartItem
        try {
            remoteProductResult = cartRemoteClient.pageQueryCartItem(userId, PRODUCT_CART_CURRENT, PRODUCT_CART_SIZE);
        } catch (Throwable ex) {
            log.error("调用购物车服务查询用户购物车商品失败", ex);
        }

        // 构造前端返回
        List<CartAdapterRespDTO> result = new ArrayList<>();
        if (remoteProductResult != null && remoteProductResult.isSuccess()) {
            result = remoteProductResult.getData().getRecords().stream().map(each -> {
                CartAdapterRespDTO cartAdapterRespDTO = new CartAdapterRespDTO();
                cartAdapterRespDTO.setProductId(each.getProductId());
                cartAdapterRespDTO.setProductName(each.getProductName());
                cartAdapterRespDTO.setProductImg(each.getProductPic());
                cartAdapterRespDTO.setProductNum(each.getProductQuantity());
                cartAdapterRespDTO.setSalePrice(each.getProductPrice().intValue());
                cartAdapterRespDTO.setLimitNum(each.getLimitNum() != null ? each.getLimitNum() : 100);
                cartAdapterRespDTO.setChecked(each.getSelectFlag() == 1 ?  "1" : "0");
                return cartAdapterRespDTO;
            }).toList();
        }
        return result;
    }

    @Override
    public Integer addProductCard(CartAddAdapterReqDTO requestParam) {
        String productId = requestParam.getProductId();
        Result<ProductRespDTO> remoteProductResult = null;

        // 调用远程服务获取商品 spu 信息
        try {
            remoteProductResult = productRemoteClient.getProductBySpuId(productId);
        } catch (Throwable ex) {
            log.error("调用商品服务查询商品详细信息失败", ex);
        }

        // 构造 req
        Result<Void> addCartResult = null;
        if (remoteProductResult != null && remoteProductResult.isSuccess()) {
            ProductRespDTO productResultData = remoteProductResult.getData();
            ProductSpuRespDTO productSpu = productResultData.getProductSpu();
            ProductSkuRespDTO productSku = productResultData.getProductSkus().get(0);

            CartItemAddReqDTO cartItemAddReqDTO = new CartItemAddReqDTO();
            BeanUtil.copyProperties(productSpu, cartItemAddReqDTO);
            cartItemAddReqDTO.setProductId(String.valueOf(productSpu.getId()));
            cartItemAddReqDTO.setProductPic(productSpu.getPic());
            cartItemAddReqDTO.setProductName(productSpu.getName());
            cartItemAddReqDTO.setProductBrand(String.valueOf(productSpu.getBrandId()));
            cartItemAddReqDTO.setProductPrice(productSku.getPrice());
            cartItemAddReqDTO.setSelectFlag(SelectFlagEnum.SELECTED.getCode());
            cartItemAddReqDTO.setProductSkuId(String.valueOf(productSku.getId()));
            cartItemAddReqDTO.setCustomerUserId(requestParam.getUserId());
            cartItemAddReqDTO.setProductQuantity(requestParam.getProductNum());
            try {
                addCartResult = cartRemoteClient.addCartItem(cartItemAddReqDTO);
            } catch (Throwable ex) {
                log.error("调用购物车服务新增购物车商品失败", ex);
            }
        }
        return (addCartResult == null || !addCartResult.isSuccess()) ? 0 : 1;
    }

    @Override
    public Integer updateProductCard(CartUpdateAdapterReqDTO requestParam) {
        Result<ProductRespDTO> remoteProductResult = null;

        // 调用远程服务获得相关 spu 信息
        try {
            remoteProductResult = productRemoteClient.getProductBySpuId(requestParam.getProductId());
        } catch (Throwable ex) {
            log.error("调用商品服务查询商品详细信息失败", ex);
        }

        // 构造 req
        int updateProductCardResult = 0;
        if (remoteProductResult != null && remoteProductResult.isSuccess()) {
            try {
                ProductRespDTO productResultData = remoteProductResult.getData();
                ProductSkuRespDTO productSkuData = productResultData.getProductSkus().get(0);
                CartItemCheckUpdateReqDTO updateCheckRequestParam = new CartItemCheckUpdateReqDTO();
                updateCheckRequestParam.setProductId(requestParam.getProductId());
                updateCheckRequestParam.setProductSkuId(String.valueOf(productSkuData.getId()));
                updateCheckRequestParam.setCustomerUserId(requestParam.getUserId());
                updateCheckRequestParam.setSelectFlag(Integer.parseInt(requestParam.getChecked()));
                cartRemoteClient.updateCheckCartItem(updateCheckRequestParam);

                CartItemNumUpdateReqDTO updateCartRequestParam = new CartItemNumUpdateReqDTO();
                updateCartRequestParam.setProductId(requestParam.getProductId());
                updateCartRequestParam.setProductSkuId(String.valueOf(productSkuData.getId()));
                updateCartRequestParam.setCustomerUserId(requestParam.getUserId());
                updateCartRequestParam.setProductQuantity(requestParam.getProductNum());
                cartRemoteClient.updateQuantityCartItem(updateCartRequestParam);
            } catch (Throwable ex) {
                log.error("调用购物车服务修改购物车商品失败", ex);
            }
            updateProductCardResult = 1;
        }
        return updateProductCardResult;
    }

    @Override
    public Integer deleteProductCard(CartDeleteAdapterReqDTO requestParam) {
        Result<ProductRespDTO> remoteProductResult = null;
        try {
            remoteProductResult = productRemoteClient.getProductBySpuId(requestParam.getProductId());
        } catch (Throwable ex) {
            log.error("调用商品服务查询商品详细信息失败", ex);
        }
        int deleteProductCardResult = 0;
        if (remoteProductResult != null && remoteProductResult.isSuccess()) {
            try {
                ProductRespDTO productResultData = remoteProductResult.getData();
                ProductSkuRespDTO productSkuData = productResultData.getProductSkus().get(0);
                CartItemDelReqDTO delCartRequestParam = new CartItemDelReqDTO();
                delCartRequestParam.setCustomerUserId(requestParam.getUserId());
                delCartRequestParam.setProductSkuIds(Lists.newArrayList(String.valueOf(productSkuData.getId())));
                cartRemoteClient.clearCartItem(delCartRequestParam);
            } catch (Throwable ex) {
                log.error("调用购物车服务删除购物车商品失败", ex);
            }
            deleteProductCardResult = 1;
        }
        return deleteProductCardResult;
    }

    @Override
    public Integer updateChecksProductCard(CartChecksAdapterReqDTO requestParam) {
        CartItemChecksUpdateReqDTO checksRequestParam = new CartItemChecksUpdateReqDTO();
        checksRequestParam.setCustomerUserId(requestParam.getUserId());
        checksRequestParam.setSelectFlag(requestParam.getChecked()
                ? SelectFlagEnum.SELECTED.getCode()
                : SelectFlagEnum.UNSELECTED.getCode()
        );
        int checksProductCardResult = 0;
        try {
            cartRemoteClient.updateChecksCartItem(checksRequestParam);
            checksProductCardResult = 1;
        } catch (Throwable ex) {
            log.error("调用购物车服务全选或取消全选购物车商品失败", ex);
        }
        return checksProductCardResult;
    }

    @Override
    public void deleteChecksProductCard(CartDeleteChecksAdapterReqDTO requestParam) {
        CartItemDelCheckReqDTO delCheckRequestParam = new CartItemDelCheckReqDTO();
        delCheckRequestParam.setCustomerUserId(requestParam.getUserId());
        try {
            cartRemoteClient.clearCheckCartItem(delCheckRequestParam);
        } catch (Throwable ex) {
            log.error("调用购物车服务删除选中购物车商品失败", ex);
        }
    }
}
