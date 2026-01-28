
package com.noinch.mall.biz.order.application.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单创建
 *
 */
@Data
public class OrderCreateCommand {
    
    @Schema(description = "用户id")
    private String customerUserId;
    
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;
    
    @Schema(description = "支付金额")
    private BigDecimal payAmount;
    
    @Schema(description = "运费金额")
    private BigDecimal freightAmount;
    
    @Schema(description = "订单来源")
    private Integer source;

    @Schema(description = "订单类型 0：正常订单 1：秒杀订单 2：促销订单")
    private Integer type;
    
    @Schema(description = "收货人")
    private String cneeName;
    
    @Schema(description = "收货人电话")
    private String cneePhone;
    
    @Schema(description = "收货人邮编")
    private String cneePostCode;
    
    @Schema(description = "收货人所在省")
    private String cneeProvinc;
    
    @Schema(description = "收货人所在市")
    private String cneeCity;
    
    @Schema(description = "收货人所在区")
    private String cneeRegion;
    
    @Schema(description = "收货人详细地址")
    private String cneeDetailAddress;
    
    @Schema(description = "订单备注信息")
    private String remark;
}
