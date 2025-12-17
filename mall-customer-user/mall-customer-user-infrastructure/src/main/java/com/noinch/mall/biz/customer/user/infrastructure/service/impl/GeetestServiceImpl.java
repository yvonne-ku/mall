package com.noinch.mall.biz.customer.user.infrastructure.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.noinch.mall.biz.customer.user.domain.dto.GeetestRespDTO;
import com.noinch.mall.biz.customer.user.domain.service.GeetestService;
import com.noinch.mall.biz.customer.user.infrastructure.config.GeetestConfig;
import com.noinch.mall.biz.customer.user.infrastructure.sdk.GeetestLib;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 极验验证码服务（基于官方SDK重写） */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeetestServiceImpl implements GeetestService {

    private final GeetestConfig geetestConfig;

    /**
     * 初始化极验验证码
     */
    @Override
    public GeetestRespDTO initGeetest() {
        if (!geetestConfig.isEnabled()) {
            log.info("极验验证码未启用，返回模拟数据");
            return createMockGeetestData();
        }

        try {
            // 创建GeetestLib实例
            GeetestLib geetestLib = new GeetestLib(geetestConfig.getCaptchaId(), geetestConfig.getPrivateKey());

            // 生成用户ID
            String userId = UUID.randomUUID().toString();

            // 初始化验证码（带用户ID）
            int result = geetestLib.preProcess(userId);

            // 获取初始化结果JSON
            String responseStr = geetestLib.getResponseStr();
            JSONObject jsonObject = JSON.parseObject(responseStr);

            // 构建响应DTO
            return GeetestRespDTO.builder()
                    .gt(geetestConfig.getCaptchaId())
                    .challenge(jsonObject.getString("challenge"))
                    .newCaptcha(true)
                    .success(result == 1)
                    .statusKey(userId)
                    .build();

        } catch (Exception e) {
            log.error("极验验证码初始化失败", e);
            return createMockGeetestData();
        }
    }

    /**
     * 验证极验验证码
     */
    @Override
    public boolean verifyGeetest(String challenge, String validate, String seccode, String statusKey) {
        if (!geetestConfig.isEnabled()) {
            log.info("极验验证码未启用，直接返回验证成功");
            return true;
        }

        if (challenge == null || validate == null || seccode == null) {
            log.warn("验证参数为空，验证失败");
            return false;
        }

        try {
            // 创建GeetestLib实例
            GeetestLib geetestLib = new GeetestLib(geetestConfig.getCaptchaId(), geetestConfig.getPrivateKey());

            // 验证验证码
            int result = geetestLib.enhencedValidateRequest(challenge, validate, seccode, statusKey);

            log.info("极验验证结果: {}", result == 1 ? "成功" : "失败");
            return result == 1;

        } catch (Exception e) {
            log.error("极验验证码验证失败", e);
            return false;
        }
    }

    /**
     * 创建模拟的极验验证码数据（用于开发环境）
     */
    private GeetestRespDTO createMockGeetestData() {
        return GeetestRespDTO.builder()
                .gt("mock-gt-id")
                .challenge(UUID.randomUUID().toString())
                .newCaptcha(true)
                .success(true)
                .statusKey(UUID.randomUUID().toString())
                .build();
    }
}