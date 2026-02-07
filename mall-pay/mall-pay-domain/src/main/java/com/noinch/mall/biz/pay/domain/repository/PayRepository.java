
package com.noinch.mall.biz.pay.domain.repository;

import com.noinch.mall.biz.pay.domain.aggregate.Pay;

/**
 * 支付仓储层
 *
 */
public interface PayRepository {

    /**
     * 根据订单号查找支付
     */
    Pay findPayByOrderSn(String orderSn);

    /**
     * 创建支付单
     *
     * @param pay 支付聚合根
     */
    void createPay(Pay pay);
    
    /**
     * 支付单回调
     *
     * @param pay 支付聚合根
     */
    void callbackPay(Pay pay);
}
