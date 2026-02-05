

package com.noinch.mall.biz.bff.remote.req;

import lombok.Data;

/**
 * 支付请求命令
 *
 */
@Data
public class PayReqDTO {
    
    /**
     * 子订单号
     */
    private String outOrderSn;
    
    /**
     * 订单总金额
     * 单位为元，精确到小数点后两位，取值范围：[0.01,100000000]
     */
    private String totalAmount;
    
    /**
     * 订单标题
     * 注意：不可使用特殊字符，如 /，=，& 等
     */
    private String subject;

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
}
