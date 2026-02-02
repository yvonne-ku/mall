
package com.noinch.mall.biz.order.domain.aggregate;

import com.noinch.mall.ddd.framework.core.domain.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 收货人信息
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CneeInfo implements ValueObject {
    
    /**
     * 收货人
     */
    private String cneeName;
    
    /**
     * 收货人电话
     */
    private String cneePhone;
    
    /**
     * 收货人邮编
     */
    private String cneePostCode;
    
    /**
     * 收货人所在省
     */
    private String cneeProvinc;
    
    /**
     * 收货人所在市
     */
    private String cneeCity;
    
    /**
     * 收货人所在区
     */
    private String cneeRegion;
    
    /**
     * 收货人详细地址
     */
    private String cneeDetailAddress;
    
    /**
     * 收货时间
     */
    private Date receiveTime;
}
