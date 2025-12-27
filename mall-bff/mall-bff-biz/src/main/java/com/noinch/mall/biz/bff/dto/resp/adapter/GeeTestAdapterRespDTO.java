
package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.Data;

/**
 * 极验验证码适配返回对象
 *
 */
@Data
public class GeeTestAdapterRespDTO {
    
    private String challenge;
    
    private String gt;
    
    private String statusKey;
    
    private Integer success;
}
