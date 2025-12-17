package com.noinch.mall.biz.customer.user.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 极验验证码初始化响应 */
@Data
@Builder
public class GeetestRespDTO {

    private String gt;

    private String challenge;

    private Boolean newCaptcha;

    private Boolean success;

    private String statusKey;
}