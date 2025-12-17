

package com.noinch.mall.biz.customer.user.application.service;

import com.noinch.mall.biz.customer.user.application.req.ReceiveAddressSaveCommand;
import com.noinch.mall.biz.customer.user.application.req.ReceiveAddressUpdateCommand;
import com.noinch.mall.biz.customer.user.application.resp.ReceiveAddressRespDTO;

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
    List<ReceiveAddressRespDTO> listReceiveAddressByCustomerUserId(String customerUserId);
    
    /**
     * 新增用户收货地址
     *
     * @param requestParam 新增收货地址请求参数
     */
    void saveReceiveAddress(ReceiveAddressSaveCommand requestParam);
    
    /**
     * 根据用户 ID、收货地址 ID 删除收货地址
     *
     * @param customerUserId   用户 ID
     * @param receiveAddressId 收货地址 ID
     */
    void removeReceiveAddress(String customerUserId, String receiveAddressId);
    
    /**
     * 修改收货地址
     *
     * @param requestParam 修改收货地址请求参数
     */
    void updateReceiveAddress(ReceiveAddressUpdateCommand requestParam);
}
