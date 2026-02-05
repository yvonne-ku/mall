

package com.noinch.mall.biz.pay.domain.base;

import lombok.Data;

/**
 * 抽象支付回调入参实体
 *
 */
@Data
public abstract class AbstractPayCallbackRequest implements PayCallbackRequest {

    private String orderRequestId;

    @Override
    public WeiXinPayCallbackRequest getWeiXinPayCallbackRequest() {
        return null;
    }
    
    @Override
    public AliPayCallbackRequest getAliPayCallbackRequest() {
        return null;
    }
    
    @Override
    public String buildMark() {
        return null;
    }
}
