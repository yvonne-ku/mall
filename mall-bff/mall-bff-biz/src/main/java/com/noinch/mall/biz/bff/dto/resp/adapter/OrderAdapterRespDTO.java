
package com.noinch.mall.biz.bff.dto.resp.adapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单适配返回对象
 *
 */
@Data
public class OrderAdapterRespDTO {
    
    private String orderId;
    
    private String orderStatus;
    
    private Integer orderTotal;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date payDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date closeDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date finishDate;
    
    private List<OrderGoodsAdapterRespDTO> goodsList;
    
    private OrderAddressAdapterRespDTO addressInfo;
}
