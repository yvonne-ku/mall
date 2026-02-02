

package com.noinch.mall.biz.bff.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 购物车勾选状态
 *
 */
@AllArgsConstructor
public enum SelectFlagEnum {
    
    /**
     * 购物车商品未选中状态
     */
    UNSELECTED(0),
    
    /**
     * 购物车商品选中状态
     */
    SELECTED(1);
    
    @Getter
    private final int code;
}
