
package com.noinch.mall.biz.bff.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用户登录状态枚举
 *
 */
@RequiredArgsConstructor
public enum UserLoginStatusEnum {
    
    /**
     * 成功
     */
    SUCCESS(1),
    
    /**
     * 失败
     */
    FAIL(0);
    
    @Getter
    private final int code;
}
