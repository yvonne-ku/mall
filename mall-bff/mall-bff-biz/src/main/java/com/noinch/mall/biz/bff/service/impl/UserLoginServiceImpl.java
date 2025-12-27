
package com.noinch.mall.biz.bff.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.noinch.mall.biz.bff.dto.req.adapter.UserRegisterAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.UserRegisterAdapterRespDTO;
import com.noinch.mall.biz.bff.remote.req.UserLoginCommand;
import com.noinch.mall.biz.bff.remote.req.UserRegisterCommand;
import com.noinch.mall.biz.bff.remote.req.UserVerifyCodeCommand;
import com.noinch.mall.biz.bff.remote.resp.GeetestRespDTO;
import com.noinch.mall.biz.bff.remote.resp.UserRegisterRespDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.bff.common.UserLoginStatusEnum;
import com.noinch.mall.biz.bff.dto.req.adapter.UserLoginAdapterRepDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.GeeTestAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.UserLoginAdapterRespDTO;
import com.noinch.mall.biz.bff.remote.CustomerUserRemoteService;
import com.noinch.mall.biz.bff.remote.resp.UserLoginRespDTO;
import com.noinch.mall.biz.bff.service.UserLoginService;

import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户登录接口实现层
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {
    
    private final CustomerUserRemoteService customerUserRemoteService;

    @Override
    public GeeTestAdapterRespDTO geetestInit() {
        Result<GeetestRespDTO> result = customerUserRemoteService.geetestInit();
        GeeTestAdapterRespDTO geeTestAdapterRespDTO = new GeeTestAdapterRespDTO();
        if (result != null && result.isSuccess()) {
            GeetestRespDTO resultData = result.getData();
            geeTestAdapterRespDTO.setChallenge(resultData.getChallenge());
            geeTestAdapterRespDTO.setGt(resultData.getGt());
            geeTestAdapterRespDTO.setStatusKey(resultData.getStatusKey());
            geeTestAdapterRespDTO.setSuccess(resultData.getSuccess() ? 1 : 0);
        }
        return geeTestAdapterRespDTO;
    }

    @Override
    public void verifyCodeSend(UserVerifyCodeCommand requestParam) {
        customerUserRemoteService.verifyCodeSend(requestParam);
    }

    @Override
    public UserRegisterAdapterRespDTO register(UserRegisterAdapterReqDTO requestParam) {
        UserRegisterCommand userRegisterCommand = new UserRegisterCommand();
        BeanUtil.copyProperties(requestParam, userRegisterCommand, CopyOptions.create().setIgnoreNullValue(true));
        Result<UserRegisterRespDTO> result = customerUserRemoteService.register(userRegisterCommand);
        UserRegisterAdapterRespDTO actualResp = new UserRegisterAdapterRespDTO();
        if (result != null && result.isSuccess()) {
            UserRegisterRespDTO resultData = result.getData();
            actualResp.setId(String.valueOf(resultData.getCustomerUserId()));
            actualResp.setUsername(resultData.getName());
            actualResp.setState(UserLoginStatusEnum.SUCCESS.getCode());
        } else {
            actualResp.setState(UserLoginStatusEnum.FAIL.getCode());
        }
        return actualResp;
    }

    @Override
    public UserLoginAdapterRespDTO login(UserLoginAdapterRepDTO requestParam) {
        UserLoginCommand userLoginCommand = new UserLoginCommand();
        BeanUtil.copyProperties(requestParam, userLoginCommand, CopyOptions.create().setIgnoreNullValue(true));
        Result<UserLoginRespDTO> result = customerUserRemoteService.login(userLoginCommand);

        UserLoginAdapterRespDTO actualResp = new UserLoginAdapterRespDTO();
        if (result != null && result.isSuccess()) {
            UserLoginRespDTO resultData = result.getData();
            actualResp.setToken(resultData.getAccessToken());
            actualResp.setId(String.valueOf(resultData.getCustomerUserId()));
            actualResp.setUsername(resultData.getAccountNumber());
            actualResp.setState(UserLoginStatusEnum.SUCCESS.getCode());
        } else {
            actualResp.setState(UserLoginStatusEnum.FAIL.getCode());
        }
        return actualResp;
    }

    @Override
    public void logout(String accessToken) {
        customerUserRemoteService.logout(accessToken);
    }

    @Override
    public UserLoginAdapterRespDTO checkLogin(String token) {
        Result<UserLoginRespDTO> result = null;
        UserLoginAdapterRespDTO actualResp = new UserLoginAdapterRespDTO();
        try {
            result = customerUserRemoteService.checkLogin(token);
        } catch (Throwable ex) {
            log.error("调用用户服务检查登录状态失败", ex);
            actualResp.setState(UserLoginStatusEnum.FAIL.getCode());
            actualResp.setMessage("检查用户登录状态失败");
        }
        if (result != null && result.isSuccess()) {
            actualResp.setMessage(Objects.isNull(result.getData()) ? "用户登录已过期" : null);
            actualResp.setState(Objects.isNull(result.getData()) ? UserLoginStatusEnum.FAIL.getCode() : UserLoginStatusEnum.SUCCESS.getCode());
            if (Objects.nonNull(result.getData())) {
                actualResp.setId(String.valueOf(result.getData().getCustomerUserId()));
                actualResp.setUsername(result.getData().getAccountNumber());
            }
        }
        actualResp.setToken(token);
        return actualResp;
    }


}
