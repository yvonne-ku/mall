

package com.noinch.mall.biz.pay.domain.base;

import lombok.Data;
import com.noinch.mall.springboot.starter.distributedid.SnowflakeIdUtil;

/**
 * 抽象支付入参实体
 *
 */
@Data
public abstract class AbstractPayRequest implements PayRequest {
    
    /**
     * 交易环境，H5、小程序、网站等
     */
    private String tradeType;
    
    /**
     * 订单号
     */
    private String orderSn;
    
    /**
     * 支付渠道
     */
    private String channel;
    
    /**
     * 商户订单号
     * 由商家自定义，64个字符以内，仅支持字母、数字、下划线且需保证在商户端不重复
     */
    private String orderRequestId = SnowflakeIdUtil.nextIdStr();

    /**
     * 在 abstract 写 return null 只是表示这个方法存在
     * 在 AliPay 的实现中，会重写这个方法，返回 AliPayRequest 实例
     zz在 WeixinPay 的实现中，会重写这个方法，仍然返回 null
     */
    @Override
    public AliPayRequest getAliPayRequest() {
        return null;
    }

    @Override
    public WeiXinPayRequest getWeiXinPayRequest() {
        return null;
    }

    @Override
    public String getOrderRequestId() {
        return orderRequestId;
    }
    
    @Override
    public String buildMark() {
        return null;
    }
}
