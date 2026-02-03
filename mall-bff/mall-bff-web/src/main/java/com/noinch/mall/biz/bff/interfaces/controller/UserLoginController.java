
package com.noinch.mall.biz.bff.interfaces.controller;


import com.noinch.mall.biz.bff.common.ResultT;
import com.noinch.mall.biz.bff.dto.req.adapter.UserRegisterAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.UserRegisterAdapterRespDTO;
import com.noinch.mall.biz.bff.remote.req.UserVerifyCodeCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.noinch.mall.biz.bff.dto.req.adapter.UserLoginAdapterRepDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.UserLoginAdapterRespDTO;
import com.noinch.mall.biz.bff.service.UserLoginService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录控制层
 *
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "用户登录")
@RequestMapping("/member")
public class UserLoginController {
    
    private final UserLoginService userLoginService;

    @PostMapping("/verify/code/send")
    @Operation(summary = "验证码发送", description = "包含注册验证码、登录验证等，发送平台包括手机、邮箱等")
    public ResultT<Void> verifyCodeSend(@RequestBody @Valid UserVerifyCodeCommand requestParam) {
        userLoginService.verifyCodeSend(requestParam);
        return ResultT.success();
    }

    @PostMapping("/register")
    @Operation(description = "用户注册")
    public ResultT<UserRegisterAdapterRespDTO> register(@RequestBody UserRegisterAdapterReqDTO requestParam) {
        UserRegisterAdapterRespDTO result = userLoginService.register(requestParam);
        return ResultT.success(result);
    }

    @PostMapping("/login")
    @Operation(description = "用户登录")
    public ResultT<UserLoginAdapterRespDTO> login(@RequestBody UserLoginAdapterRepDTO requestParam) {
        UserLoginAdapterRespDTO result = userLoginService.login(requestParam);
        return ResultT.success(result);
    }

    @GetMapping("/checkLogin")
    @Operation(description = "检查用户是否登录")
    public ResultT<UserLoginAdapterRespDTO> checkLogin(@RequestParam(value = "token", required = false) String token) {
        UserLoginAdapterRespDTO result = userLoginService.checkLogin(token);
        return ResultT.success(result);
    }
    
    @GetMapping("/loginOut")
    @Operation(description = "用户退出登录")
    public ResultT<Void> logout(@RequestParam(value = "token", required = false) String token) {
        userLoginService.logout(token);
        return ResultT.success();
    }
}
