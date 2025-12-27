
package com.noinch.mall.biz.customer.user.interfaces.controller;

import com.noinch.mall.biz.customer.user.application.req.UserRegisterCommand;
import com.noinch.mall.biz.customer.user.application.req.UserVerifyCodeCommand;
import com.noinch.mall.biz.customer.user.domain.dto.GeetestRespDTO;
import com.noinch.mall.biz.customer.user.application.resp.UserRegisterRespDTO;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.customer.user.application.req.UserLoginCommand;
import com.noinch.mall.biz.customer.user.application.resp.UserLoginRespDTO;
import com.noinch.mall.biz.customer.user.application.service.CustomerUserService;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.web.Results;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * C端用户登录控制层
 * */
@RestController
@RequiredArgsConstructor
public class CustomerUserLoginController {

    private final CustomerUserService customerUserService;

    @GetMapping("/api/customer-user/geetestInit")
    @Operation(summary = "初始化极验验证码", description = "初始化极验验证码")
    public Result<GeetestRespDTO> geetestInit() {
        GeetestRespDTO result = customerUserService.initGeetest();
        return Results.success(result);
    }

    @PostMapping("/api/customer-user/verify/code/send")
    @Operation(summary = "验证码发送", description = "包含注册验证码、登录验证等，发送平台包括手机、邮箱等")
    public Result<Void> verifyCodeSend(@RequestBody @Valid UserVerifyCodeCommand requestParam) {
        customerUserService.verifyCodeSend(requestParam);
        return Results.success();
    }

    @PostMapping("/api/customer-user/register")
    @Operation(summary = "注册用户", description = "注册C端用户账号")
    public Result<UserRegisterRespDTO> register(@RequestBody @Valid UserRegisterCommand requestParam) {
        // 检验极验验证码二次验证
        boolean geetestResult = customerUserService.verifyGeetest(requestParam.getChallenge(), requestParam.getValidate(), requestParam.getSeccode(), requestParam.getStatusKey());
        if (!geetestResult) {
            throw new ClientException(BaseErrorCode.GEETEST_ERROR);
        }
        // 注册用户
        UserRegisterRespDTO result = customerUserService.register(requestParam);
        return Results.success(result);
    }

    @PostMapping("/api/customer-user/login")
    @Operation(summary = "用户登录", description = "用户登录")
    public Result<UserLoginRespDTO> login(@RequestBody @Valid UserLoginCommand requestParam) {
        // 检验极验验证码二次验证
        boolean geetestResult = customerUserService.verifyGeetest(requestParam.getChallenge(), requestParam.getValidate(), requestParam.getSeccode(), requestParam.getStatusKey());
        if (!geetestResult) {
            throw new ClientException(BaseErrorCode.GEETEST_ERROR);
        }
        // 登录用户
        UserLoginRespDTO result = customerUserService.login(requestParam);
        return Results.success(result);
    }

    @GetMapping("/api/customer-user/logout")
    @Operation(summary = "用户退出登录", description = "用户退出登录")
    @Parameter(
            name = "accessToken",
            description = "用户Token",
            required = true,
            example = "JWT Token"
    )
    public Result<Void> logout(@RequestParam(required = false) String accessToken) {
        customerUserService.logout(accessToken);
        return Results.success();
    }

    @GetMapping("/api/customer-user/check-login")
    @Operation(summary = "检查用户是否登录", description = "通过Token检查用户是否登录")
    @Parameter(
            name = "accessToken",
            description = "用户Token",
            required = true,
            example = "JWT Token"
    )
    public Result<UserLoginRespDTO> checkLogin(@RequestParam("accessToken") String accessToken) {
        UserLoginRespDTO result = customerUserService.checkLogin(accessToken);
        return Results.success(result);
    }
}
