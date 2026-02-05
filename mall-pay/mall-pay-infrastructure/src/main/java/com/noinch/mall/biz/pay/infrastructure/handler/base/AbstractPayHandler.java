
package com.noinch.mall.biz.pay.infrastructure.handler.base;

import com.noinch.mall.biz.pay.domain.base.PayRequest;
import com.noinch.mall.biz.pay.domain.base.PayResponse;

/**
 * 抽象支付组件
 *
 */
public abstract class AbstractPayHandler {
    
    /**
     * 支付抽象接口
     *
     * @param payRequest 支付请求参数
     * @return 支付响应参数
     */
    public abstract PayResponse pay(PayRequest payRequest);
}
