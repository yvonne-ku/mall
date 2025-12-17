

package com.noinch.mall.biz.customer.user.domain.mode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.noinch.mall.ddd.framework.core.domain.Entity;

/**
 * 收货地址
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveAddress implements Entity {
    
    /**
     * id
     */
    private String id;
    
    /**
     * c端用户 id
     */
    private Long customerUserId;
    
    /**
     * 收货人名称
     */
    private String name;
    
    /**
     * 收货人电话
     */
    private String phone;
    
    /**
     * 是否默认 0：否 1：是
     */
    private int defaultFlag;
    
    /**
     * 标签 0：家 1：公司
     */
    private int tag;
    
    /**
     * 邮政编码
     */
    private String postCode;
    
    /**
     * 省
     */
    private String province;
    
    /**
     * 市
     */
    private String city;
    
    /**
     * 区
     */
    private String region;
    
    /**
     * 详细地址
     */
    private String detailAddress;
}
