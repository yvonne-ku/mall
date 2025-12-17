
package com.noinch.mall.biz.customer.user.interfaces.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.customer.user.application.req.ReceiveAddressSaveCommand;
import com.noinch.mall.biz.customer.user.application.req.ReceiveAddressUpdateCommand;
import com.noinch.mall.biz.customer.user.application.resp.ReceiveAddressRespDTO;
import com.noinch.mall.biz.customer.user.application.service.ReceiveAddressService;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.web.Results;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址控制层
 * */
@RestController
@AllArgsConstructor
@Api(tags = "收货地址")
public class ReceiveAddressController {
    
    private final ReceiveAddressService receiveAddressService;
    
    @GetMapping("/api/customer-user/receive-address/{customerUserId}")
    @ApiOperation(value = "获取用户收货地址", notes = "根据用户ID获取用户收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerUserId", value = "用户ID", required = true, example = "1634554535496892416")
    })
    public Result<List<ReceiveAddressRespDTO>> listReceiveAddress(@PathVariable("customerUserId") String customerUserId) {
        return Results.success(receiveAddressService.listReceiveAddressByCustomerUserId(customerUserId));
    }
    
    @PostMapping("/api/customer-user/receive-address")
    @ApiOperation(value = "新增用户收货地址", notes = "新增用户收货地址")
    public Result<Void> saveReceiveAddress(@RequestBody ReceiveAddressSaveCommand requestParam) {
        receiveAddressService.saveReceiveAddress(requestParam);
        return Results.success();
    }
    
    @PutMapping("/api/customer-user/receive-address")
    @ApiOperation(value = "修改用户收货地址", notes = "修改用户收货地址")
    public Result<Void> updateReceiveAddress(@RequestBody ReceiveAddressUpdateCommand requestParam) {
        receiveAddressService.updateReceiveAddress(requestParam);
        return Results.success();
    }
    
    @DeleteMapping("/api/customer-user/{customerUserId}/receive-address/{receiveAddressId}")
    @ApiOperation(value = "删除用户收货地址", notes = "根据用户ID和收货ID删除用户收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerUserId", value = "用户ID", required = true, example = "1634554535496892416"),
            @ApiImplicitParam(name = "receiveAddressId", value = "收货地址ID", required = true, example = "1634561618543894528")
    })
    public Result<Void> removeReceiveAddress(@PathVariable("customerUserId") String customerUserId, @PathVariable("receiveAddressId") String receiveAddressId) {
        receiveAddressService.removeReceiveAddress(customerUserId, receiveAddressId);
        return Results.success();
    }
}
