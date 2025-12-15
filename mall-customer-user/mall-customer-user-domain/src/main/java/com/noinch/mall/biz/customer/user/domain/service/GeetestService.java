package com.noinch.mall.biz.customer.user.domain.service;

import com.noinch.mall.biz.customer.user.domain.dto.GeetestRespDTO;

public interface GeetestService {

    /**
     * 初始化极验验证码
     * @return 验证码响应DTO
     */
    GeetestRespDTO initGeetest();

    /**
     * 验证极验验证码
     * @param challenge 挑战码
     * @param validate 验证码
     * @param seccode 安全码
     * @param statusKey 状态Key
     * @return 验证结果
     */
    boolean verifyGeetest(String challenge, String validate, String seccode, String statusKey);
}
