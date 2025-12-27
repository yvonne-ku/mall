package com.noinch.mall.biz.customer.user.infrastructure.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.noinch.mall.biz.customer.user.domain.dto.GeetestRespDTO;
import com.noinch.mall.biz.customer.user.domain.service.GeetestService;
import com.noinch.mall.biz.customer.user.infrastructure.config.GeetestConfig;
import com.noinch.mall.biz.customer.user.infrastructure.sdk.GeetestLib;
import com.noinch.mall.springboot.starter.cache.DistributedCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 极验验证码服务（基于官方SDK重写）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeetestServiceImpl implements GeetestService {

    private final GeetestConfig geetestConfig;

    private final DistributedCache distributedCache;

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

            // statusKey, 用于后续验证，防止跨站请求伪造（CSRF）攻击
            String userId = UUID.randomUUID().toString();

            // 调用 preProcess 方法，获取服务器状态码（1表示需要重新获取验证码，0表示可以直接展示验证码）
            // 存入缓存，为后续验证时能够校验用户是否是合法请求
            int gtServerStatus = geetestLib.preProcess(userId);
            distributedCache.put(userId, String.valueOf(gtServerStatus), 360, TimeUnit.SECONDS);

            // 调用 getResponseStr 方法，获取初始化结果 JSON 字符串
            // 从响应字符串中提取 gt、challenge、newCaptcha、success 字段，并构建 GeetestRespDTO 对象
            String responseStr = geetestLib.getResponseStr();
            GeetestRespDTO geetestRespDTO = JSON.parseObject(responseStr, GeetestRespDTO.class);
            geetestRespDTO.setStatusKey(userId);
            return geetestRespDTO;

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
            GeetestLib geetestLib = new GeetestLib(geetestConfig.getCaptchaId(), geetestConfig.getPrivateKey());

            // 从缓存中获取服务器状态码
            // 0 表示可以直接展示验证码，1 表示需要重新获取验证码
            int gtServerStatus = Integer.parseInt(distributedCache.get(statusKey, String.class));
            int gtResult = -1;
            if (gtServerStatus == 1) {
                // 调用 enhencedValidateRequest 方法，进行验证码验证
                gtResult = geetestLib.enhencedValidateRequest(challenge, validate, seccode, statusKey);
            }
            if (gtServerStatus == 0) {
                // 调用 failBackValidateRequest 方法，进行验证码验证
                gtResult = geetestLib.failbackValidateRequest(challenge, validate, seccode);
            }
            log.info("极验验证结果: {}", gtResult == 1 ? "成功" : "失败");
            return gtResult == 1;

        } catch (Exception e) {
            log.error("极验验证码验证失败", e);
            return false;
        }
    }

    /**
     * 创建模拟的极验验证码数据（用于开发环境）
     */
    private GeetestRespDTO createMockGeetestData() {
        GeetestRespDTO geetestRespDTO = new GeetestRespDTO();
        geetestRespDTO.setGt("mock-gt-id");
        geetestRespDTO.setChallenge(UUID.randomUUID().toString());
        geetestRespDTO.setSuccess(true);
        geetestRespDTO.setStatusKey(UUID.randomUUID().toString());
        return geetestRespDTO;
    }
}