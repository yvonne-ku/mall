
package com.noinch.mall.biz.bff.remote.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户收货地址出参
 *
 */
@Data
public class ReceiveAddressRespDTO {
    
    @Schema(description = "收货地址 id")
    private String id;
    
    @Schema(description = "c端用户 id")
    private String customerUserId;
    
    @Schema(description = "收货人名称")
    private String name;
    
    @Schema(description = "收货人电话")
    private String phone;
    
    @Schema(description = "是否默认 0：否 1：是")
    private Integer defaultFlag;
    
    @Schema(description = "标签 0：家 1：公司")
    private Integer tag;
    
    @Schema(description = "邮政编码")
    private String postCode;
    
    @Schema(description = "省")
    private String province;
    
    @Schema(description = "市")
    private String city;
    
    @Schema(description = "区")
    private String region;
    
    @Schema(description = "详细地址")
    private String detailAddress;
}
