

package com.noinch.mall.biz.bff.service;

import com.noinch.mall.biz.bff.dto.req.adapter.ReceiveAddressSaveAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.ReceiveAddressUpdateAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.ReceiveAddressAdapterRespDTO;

import java.util.List;

/**
 * 用户收货地址
 *
 */
public interface ReceiveAddressService {
    
    /**
     * 根据用户 ID 查询收货地址
     *
     * @param customerUserId 用户 ID
     * @return 用户收货地址集合
     */
    List<ReceiveAddressAdapterRespDTO> listReceiveAddressByCustomerUserId(String customerUserId);
    
    /**
     * 新增用户收货地址
     *
     * @param requestParam 新增收货地址请求参数
     */
    Integer saveReceiveAddress(ReceiveAddressSaveAdapterReqDTO requestParam);
    
    /**
     * 根据用户 ID、收货地址 ID 删除收货地址
     *
     * @param userId 用户 ID
     * @param receiveAddressId 收货地址 ID
     */
    Integer removeReceiveAddress(String userId, String receiveAddressId);
    
    /**
     * 修改收货地址
     *
     * @param requestParam 修改收货地址请求参数
     */
    Integer updateReceiveAddress(ReceiveAddressUpdateAdapterReqDTO requestParam);
}
