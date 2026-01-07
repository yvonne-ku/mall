package com.noinch.mall.biz.customer.user.domain.service;

public interface GeetestService {

    /**
     * 验证极验验证码
     * @param captchaId 验证码ID
     * @param lotNumber 随机数
     * @param passToken 传递令牌
     * @param genTime 生成时间
     * @param captchaOutput 验证码输出
     * @return 验证结果
     */
    boolean verifyGeetest(String captchaId, String lotNumber, String passToken, String genTime, String captchaOutput);
}
