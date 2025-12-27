package com.noinch.mall.biz.bff.remote.resp;

import lombok.Data;

/**
 * 极验验证码初始化响应 */
@Data
public class GeetestRespDTO {

    private String gt;

    private String challenge;

    private String statusKey;

    private Boolean success;
}