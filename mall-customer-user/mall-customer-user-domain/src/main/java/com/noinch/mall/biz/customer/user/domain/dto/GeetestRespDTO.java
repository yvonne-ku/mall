package com.noinch.mall.biz.customer.user.domain.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 极验验证码初始化响应 */
@Data
public class GeetestRespDTO {

    private String gt;

    private String challenge;

    @JSONField(name = "new_captcha")
    private Boolean newCaptcha;

    private String statusKey;

    private Boolean success;
}