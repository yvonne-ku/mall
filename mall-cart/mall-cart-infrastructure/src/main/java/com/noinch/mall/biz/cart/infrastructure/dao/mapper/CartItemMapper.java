

package com.noinch.mall.biz.cart.infrastructure.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.noinch.mall.biz.cart.infrastructure.dao.entity.CartItemDO;

/**
 * 商品购物车
 *
 */
public interface CartItemMapper extends BaseMapper<CartItemDO> {
    
    /**
     * 统计用户购物车商品数量
     */
    Integer countUserCartItem(String customerUserId);
}
