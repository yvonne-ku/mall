

package com.noinch.mall.biz.pay.domain.base;

/**
 * 支付回调请求入参
 *
 */
public interface PayCallbackRequest {
    
    /**
     * 获取阿里支付回调入参
     */
    AliPayCallbackRequest getAliPayCallbackRequest();
    
    /**
     * 获取微信支付回调入参
     */
    WeiXinPayCallbackRequest getWeiXinPayCallbackRequest();

    /**
     * 构建查找支付回调策略实现类标识
     */
    String buildMark();
}
