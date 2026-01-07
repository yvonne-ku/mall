

package com.noinch.mall.biz.bff.service;

import com.noinch.mall.biz.bff.dto.req.adapter.UserLoginAdapterRepDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.UserRegisterAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.UserLoginAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.UserRegisterAdapterRespDTO;
import com.noinch.mall.biz.bff.remote.req.UserVerifyCodeCommand;

/**
 * 用户登录接口
 *
 */
public interface UserLoginService {

    /**
     * 发送验证码
     *
     * @param requestParam 验证码入参
     */
    void verifyCodeSend(UserVerifyCodeCommand requestParam);

    /**
     * 用户注册
     *
     * @param requestParam 用户注册入参
     * @return 用户注册返回信息
     */
    UserRegisterAdapterRespDTO register(UserRegisterAdapterReqDTO requestParam);

    /**
     * C 端用户登录
     *
     * @param requestParam 用户登录入参
     * @return 用户登录返回信息
     */
    UserLoginAdapterRespDTO login(UserLoginAdapterRepDTO requestParam);
    
    /**
     * 检查 C 端用户是否登录
     *
     * @param token JWT Token
     * @return 用户是否登录
     */
    UserLoginAdapterRespDTO checkLogin(String token);

    /**
     * 用户退出登录
     *
     * @param accessToken 用户登录 Token
     */
    void logout(String accessToken);

}
