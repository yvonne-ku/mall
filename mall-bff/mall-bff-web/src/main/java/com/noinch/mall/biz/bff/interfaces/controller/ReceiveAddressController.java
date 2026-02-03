package com.noinch.mall.biz.bff.interfaces.controller;


import com.noinch.mall.biz.bff.common.ResultT;
import com.noinch.mall.biz.bff.dto.req.adapter.ReceiveAddressDeleteAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.ReceiveAddressQueryAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.ReceiveAddressSaveAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.ReceiveAddressUpdateAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.ReceiveAddressAdapterRespDTO;
import com.noinch.mall.biz.bff.service.ReceiveAddressService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 收货地址控制层
 *
 */
@RestController(value = "bffReceiveAddressController")
@AllArgsConstructor
@Tag(name = "收货地址")
public class ReceiveAddressController {

    private final ReceiveAddressService bffReceiveAddressService;

    @PostMapping("/member/addressList")
    @Schema(description = "根据用户ID获取用户收货地址")
    public ResultT<List<ReceiveAddressAdapterRespDTO>> listReceiveAddress(@RequestBody ReceiveAddressQueryAdapterReqDTO requestParam) {
        return ResultT.success(bffReceiveAddressService.listReceiveAddressByCustomerUserId(requestParam.getUserId()));
    }

    @PostMapping("/member/addAddress")
    @Schema(description = "新增用户收货地址")
//    @SentinelResource(
//            value = ADD_ADDRESS_PATH,
//            blockHandler = "addAddressBlockHandlerMethod",
//            blockHandlerClass = CustomBlockHandler.class
//    )
    public ResultT<Integer> saveReceiveAddress(@RequestBody ReceiveAddressSaveAdapterReqDTO requestParam) {
        return ResultT.success(bffReceiveAddressService.saveReceiveAddress(requestParam));
    }

    @PostMapping("/member/updateAddress")
    @Schema(description = "修改用户收货地址")
    public ResultT<Integer> updateReceiveAddress(@RequestBody ReceiveAddressUpdateAdapterReqDTO requestParam) {
        return ResultT.success(bffReceiveAddressService.updateReceiveAddress(requestParam));
    }

    @PostMapping("/member/delAddress")
    @Schema(description = "根据用户ID和收货ID删除用户收货地址")
    public ResultT<Integer> removeReceiveAddress(@RequestBody ReceiveAddressDeleteAdapterReqDTO requestParam) {
        return ResultT.success(bffReceiveAddressService.removeReceiveAddress(requestParam.getUserId(), requestParam.getAddressId()));
    }
}

