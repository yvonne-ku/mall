
package com.noinch.mall.springboot.starter.idempotent.core.token.service;

import com.noinch.mall.springboot.starter.idempotent.core.IdempotentExecuteHandler;

/**
 * Token 实现幂等接口
 *
 */
public interface IdempotentTokenService extends IdempotentExecuteHandler {
    
    /**
     * 创建幂等验证Token
     */
    String createToken();
}
