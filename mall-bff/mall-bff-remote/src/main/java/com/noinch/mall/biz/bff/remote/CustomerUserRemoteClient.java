
package com.noinch.mall.biz.bff.remote;

import com.noinch.mall.biz.bff.remote.req.*;
import com.noinch.mall.biz.bff.remote.resp.GeetestRespDTO;
import com.noinch.mall.biz.bff.remote.resp.ReceiveAddressRespDTO;
import com.noinch.mall.biz.bff.remote.resp.UserLoginRespDTO;
import com.noinch.mall.biz.bff.remote.resp.UserRegisterRespDTO;
import com.noinch.mall.springboot.starter.convention.result.Result;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端用户远程服务调用
 *
 */
//@FeignClient(value = "customer-user-service")
@FeignClient(value = "customer-user-service", url = "${mall.remote-url.customer-user-service:}")
public interface CustomerUserRemoteClient {

    @PostMapping("/api/customer-user/verify/code/send")
    Result<Void> verifyCodeSend(@RequestBody @Valid UserVerifyCodeCommand requestParam);

    /**
     * 初始化极验验证码
     */
    @GetMapping("/api/customer-user/geetestInit")
    Result<GeetestRespDTO> geetestInit();

    /**
     * 用户登录
     */
    @PostMapping("/api/customer-user/login")
    Result<UserLoginRespDTO> login(@RequestBody UserLoginCommand requestParam);

    /**
     * 用户注册
     */
    @PostMapping("/api/customer-user/register")
    Result<UserRegisterRespDTO> register(@RequestBody UserRegisterCommand requestParam);

    /**
     * 检查用户是否登录
     */
    @GetMapping("/api/customer-user/check-login")
    Result<UserLoginRespDTO> checkLogin(@RequestParam("accessToken") String accessToken);
    
    /**
     * 用户退出登录
     */
    @GetMapping("/api/customer-user/logout")
    Result<Void> logout(@RequestParam("accessToken") String accessToken);
    
    /**
     * 根据用户 ID 查询收货地址
     */
    @GetMapping("/api/customer-user/receive-address/{customerUserId}")
    Result<List<ReceiveAddressRespDTO>> listReceiveAddress(@PathVariable("customerUserId") String customerUserId);
    
    /**
     * 新增用户收货地址
     */
    @PostMapping("/api/customer-user/receive-address")
    Result<Void> saveReceiveAddress(@RequestBody ReceiveAddressSaveCommand requestParam);
    
    /**
     * 修改用户收货地址
     */
    @PutMapping("/api/customer-user/receive-address")
    Result<Void> updateReceiveAddress(@RequestBody ReceiveAddressUpdateCommand requestParam);
    
    /**
     * 删除用户收货地址
     */
    @DeleteMapping("/api/customer-user/{customerUserId}/receive-address/{receiveAddressId}")
    Result<Void> removeReceiveAddress(@PathVariable("customerUserId") String customerUserId, @PathVariable("receiveAddressId") String receiveAddressId);
}
