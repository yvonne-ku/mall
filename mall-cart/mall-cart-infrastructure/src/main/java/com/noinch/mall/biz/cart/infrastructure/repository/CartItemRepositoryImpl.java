
package com.noinch.mall.biz.cart.infrastructure.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.noinch.mall.springboot.starter.mybatisplus.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import com.noinch.mall.biz.cart.domain.aggregate.CartItem;
import com.noinch.mall.biz.cart.domain.common.SelectFlagEnum;
import com.noinch.mall.biz.cart.domain.repository.CartItemRepository;
import com.noinch.mall.biz.cart.infrastructure.dao.entity.CartItemDO;
import com.noinch.mall.biz.cart.infrastructure.dao.mapper.CartItemMapper;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import com.noinch.mall.springboot.starter.convention.page.PageRequest;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 购物车仓储层
 *
 */
@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository {
    
    private final CartItemMapper cartItemMapper;
    private final RedissonClient redissonClient;
    
    private static final String ADD_CART_LOCK_PREFIX = "add-cart-lock_";
    
    @Value("${mall.cart.max-product:500}")
    public Integer maxProductNum;
    
    @Override
    public PageResponse<CartItem> pageQueryCartItem(String userId, PageRequest pageRequest) {
        LambdaQueryWrapper<CartItemDO> queryWrapper = Wrappers.lambdaQuery(CartItemDO.class)
                .eq(CartItemDO::getCustomerUserId, Long.parseLong(userId));
        Page<CartItemDO> selectPage = cartItemMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(selectPage, CartItem.class);
    }

    @Override
    public List<CartItem> querySelectCartItemByCustomerUserId(String customerUserId) {
        LambdaQueryWrapper<CartItemDO> queryWrapper = Wrappers.lambdaQuery(CartItemDO.class)
                .eq(CartItemDO::getCustomerUserId, customerUserId)
                .eq(CartItemDO::getSelectFlag, SelectFlagEnum.SELECTED.getCode());
        List<CartItemDO> selectList = cartItemMapper.selectList(queryWrapper);
        return selectList.stream().map(each -> {
            CartItem cartItem = new CartItem();
            BeanUtil.copyProperties(each, cartItem);
            return cartItem;
        }).collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public void addCartItem(CartItem cartItem) {
        String beforeLockName = StrUtil.builder()
                .append(cartItem.getCustomerUserId())
                .append(cartItem.getProductId())
                .append(cartItem.getProductSkuId())
                .toString();
        String actualLockName = StrUtil.builder()
                .append(ADD_CART_LOCK_PREFIX)
                .append(Base64.encodeStr(beforeLockName.getBytes(), true, false))
                .toString();
        RLock lock = redissonClient.getLock(actualLockName);
        boolean tryLock = lock.tryLock(3, TimeUnit.SECONDS);
        if (!tryLock) {
            throw new ServiceException("添加购物车失败");
        }

        int countUserCartItem = countCartItem(String.valueOf(cartItem.getCustomerUserId()));
        if (countUserCartItem >= maxProductNum) {
            throw new ServiceException(String.format("购物车最多添加%d件商品", maxProductNum));
        }
        try {
            LambdaQueryWrapper<CartItemDO> queryWrapper = Wrappers.lambdaQuery(CartItemDO.class)
                    .eq(CartItemDO::getProductId, cartItem.getProductId())
                    .eq(CartItemDO::getProductSkuId, cartItem.getProductSkuId())
                    .eq(CartItemDO::getCustomerUserId, cartItem.getCustomerUserId());
            CartItemDO cartItemDO = cartItemMapper.selectOne(queryWrapper);
            if (cartItemDO != null) {
                LambdaUpdateWrapper<CartItemDO> updateWrapper = Wrappers.lambdaUpdate(CartItemDO.class)
                        .eq(CartItemDO::getProductId, cartItem.getProductId())
                        .eq(CartItemDO::getProductSkuId, cartItem.getProductSkuId())
                        .eq(CartItemDO::getCustomerUserId, cartItem.getCustomerUserId());
                CartItemDO updateCartItem = new CartItemDO();
                updateCartItem.setProductQuantity(cartItemDO.getProductQuantity() + cartItem.getProductQuantity());
                cartItemMapper.update(updateCartItem, updateWrapper);
            } else {
                cartItemDO = new CartItemDO();
                BeanUtil.copyProperties(cartItem, cartItemDO);
                int insertFlag = cartItemMapper.insert(cartItemDO);
                Assert.isTrue(insertFlag > 0, () -> new ServiceException("添加购物车失败"));
            }
        } catch (Exception e) {
            throw new ServiceException("添加购物车失败");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateCheckCartItem(CartItem cartItem) {
        LambdaUpdateWrapper<CartItemDO> updateWrapper = Wrappers.lambdaUpdate(CartItemDO.class)
                .eq(CartItemDO::getProductId, cartItem.getProductId())
                .eq(CartItemDO::getProductSkuId, cartItem.getProductSkuId())
                .eq(CartItemDO::getCustomerUserId, cartItem.getCustomerUserId());
        CartItemDO updateCartItem = new CartItemDO();
        updateCartItem.setSelectFlag(cartItem.getSelectFlag());
        int updateFlag = cartItemMapper.update(updateCartItem, updateWrapper);
        Assert.isTrue(updateFlag > 0, () -> new ServiceException("修改购物车选中状态失败"));
    }

    @Override
    public void updateChecksCartItem(CartItem cartItem) {
        LambdaUpdateWrapper<CartItemDO> updateWrapper = Wrappers.lambdaUpdate(CartItemDO.class)
                .eq(CartItemDO::getCustomerUserId, cartItem.getCustomerUserId());
        CartItemDO updateCartItem = new CartItemDO();
        updateCartItem.setSelectFlag(cartItem.getSelectFlag());
        int updateFlag = cartItemMapper.update(updateCartItem, updateWrapper);
        Assert.isTrue(updateFlag > 0, () -> new ServiceException("修改购物车选中状态失败"));
    }

    @Override
    public void updateCartItem(CartItem cartItem) {
        LambdaUpdateWrapper<CartItemDO> updateWrapper = Wrappers.lambdaUpdate(CartItemDO.class)
                .eq(CartItemDO::getProductId, cartItem.getProductId())
                .eq(CartItemDO::getProductSkuId, cartItem.getProductSkuId())
                .eq(CartItemDO::getCustomerUserId, cartItem.getCustomerUserId());
        CartItemDO updateCartItem = new CartItemDO();
        updateCartItem.setProductQuantity(cartItem.getProductQuantity());
        int updateFlag = cartItemMapper.update(updateCartItem, updateWrapper);
        Assert.isTrue(updateFlag > 0, () -> new ServiceException("修改购物车失败"));
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        LambdaUpdateWrapper<CartItemDO> updateWrapper = Wrappers.lambdaUpdate(CartItemDO.class)
                .eq(CartItemDO::getCustomerUserId, cartItem.getCustomerUserId())
                .in(CartItemDO::getProductSkuId, cartItem.getProductSkuIds());
        int updateFlag = cartItemMapper.delete(updateWrapper);
        Assert.isTrue(updateFlag > 0, () -> new ServiceException("删除购物车失败"));
    }

    @Override
    public int countCartItem(String customerUserId) {
        return Optional.ofNullable(cartItemMapper.countCartItem(customerUserId)).orElse(0);
    }

    @Override
    public void deleteCheckCartItem(CartItem cartItem) {
        LambdaUpdateWrapper<CartItemDO> updateWrapper = Wrappers.lambdaUpdate(CartItemDO.class)
                .eq(CartItemDO::getCustomerUserId, cartItem.getCustomerUserId())
                .eq(CartItemDO::getSelectFlag, SelectFlagEnum.SELECTED.getCode());
        int updateFlag = cartItemMapper.delete(updateWrapper);
        Assert.isTrue(updateFlag > 0, () -> new ServiceException("删除购物车选中商品失败"));
    }
}
