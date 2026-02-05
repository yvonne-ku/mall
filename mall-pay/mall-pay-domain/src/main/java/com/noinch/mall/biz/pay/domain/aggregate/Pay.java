

package com.noinch.mall.biz.pay.domain.aggregate;

import com.noinch.mall.ddd.framework.core.domain.AggregateRoot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付聚合根
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pay implements AggregateRoot {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 订单号
     */
    private String orderSn;
    
    /**
     * 商户订单号
     */
    private String outOrderSn;
    
    /**
     * 支付渠道
     */
    private String channel;
    
    /**
     * 支付环境
     */
    private String tradeType;
    
    /**
     * 订单标题
     */
    private String subject;
    
    /**
     * 交易凭证号
     */
    private String tradeNo;
    
    /**
     * 交易总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 付款时间
     */
    private Date gmtPayment;
    
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    
    /**
     * 支付状态
     */
    private String status;
    
    /**
     * 商户订单号
     */
    private String orderRequestId;
}
