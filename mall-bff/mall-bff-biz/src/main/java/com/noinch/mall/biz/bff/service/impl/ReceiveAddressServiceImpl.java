
package com.noinch.mall.biz.bff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.noinch.mall.biz.bff.dto.req.adapter.ReceiveAddressSaveAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.req.adapter.ReceiveAddressUpdateAdapterReqDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.ReceiveAddressAdapterRespDTO;
import com.noinch.mall.biz.bff.remote.CustomerUserRemoteService;
import com.noinch.mall.biz.bff.remote.req.ReceiveAddressSaveCommand;
import com.noinch.mall.biz.bff.remote.req.ReceiveAddressUpdateCommand;
import com.noinch.mall.biz.bff.remote.resp.ReceiveAddressRespDTO;
import com.noinch.mall.biz.bff.service.ReceiveAddressService;
import com.noinch.mall.springboot.starter.convention.result.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户收货地址
 *
 */
@Slf4j
@Service("bffReceiveAddressService")
@RequiredArgsConstructor
public class ReceiveAddressServiceImpl implements ReceiveAddressService {
    
    private final CustomerUserRemoteService customerUserRemoteService;
    
    @Override
    public List<ReceiveAddressAdapterRespDTO> listReceiveAddressByCustomerUserId(String customerUserId) {
        Result<List<ReceiveAddressRespDTO>> receiveAddressRemoteResult = customerUserRemoteService.listReceiveAddress(customerUserId);
        List<ReceiveAddressAdapterRespDTO> result = new ArrayList<>();
        if (receiveAddressRemoteResult.isSuccess() && receiveAddressRemoteResult.getData() != null) {
            List<ReceiveAddressRespDTO> addressRemoteResultData = receiveAddressRemoteResult.getData();
            addressRemoteResultData.forEach(each -> {
                ReceiveAddressAdapterRespDTO resultData = new ReceiveAddressAdapterRespDTO();
                resultData.setAddressId(each.getId());
                resultData.setTel(each.getPhone());
                resultData.setUserId(each.getCustomerUserId());
                resultData.setUserName(each.getName());
                resultData.setStreetName(each.getDetailAddress());
                resultData.setIsDefault(each.getDefaultFlag() == 1 ? true : false);
                result.add(resultData);
            });
        }
        return result;
    }
    
    @Override
    public Integer saveReceiveAddress(ReceiveAddressSaveAdapterReqDTO requestParam) {
        int saveReceiveAddressFlag = 0;
        try {
            ReceiveAddressSaveCommand remoteRequestParam = new ReceiveAddressSaveCommand();
            remoteRequestParam.setName(requestParam.getUserName());
            remoteRequestParam.setDetailAddress(requestParam.getStreetName());
            remoteRequestParam.setPhone(requestParam.getTel());
            remoteRequestParam.setCustomerUserId(requestParam.getUserId());
            remoteRequestParam.setDefaultFlag(requestParam.getIsDefault() ? 1 : 0);
            customerUserRemoteService.saveReceiveAddress(remoteRequestParam);
            saveReceiveAddressFlag = 1;
        } catch (Throwable ex) {
            log.error("调用用户服务新增收货地址失败", ex);
        }
        return saveReceiveAddressFlag;
    }
    
    @Override
    public Integer removeReceiveAddress(String userId, String receiveAddressId) {
        int removeReceiveAddressFlag = 0;
        try {
            customerUserRemoteService.removeReceiveAddress(userId, receiveAddressId);
            removeReceiveAddressFlag = 1;
        } catch (Throwable ex) {
            log.error("调用用户服务删除收货地址失败", ex);
        }
        return removeReceiveAddressFlag;
    }
    
    @Override
    public Integer updateReceiveAddress(ReceiveAddressUpdateAdapterReqDTO requestParam) {
        int updateReceiveAddressFlag = 0;
        try {
            ReceiveAddressUpdateCommand remoteRequestParam = new ReceiveAddressUpdateCommand();
            remoteRequestParam.setId(requestParam.getAddressId());
            remoteRequestParam.setName(requestParam.getUserName());
            remoteRequestParam.setDetailAddress(requestParam.getStreetName());
            remoteRequestParam.setPhone(requestParam.getTel());
            remoteRequestParam.setCustomerUserId(requestParam.getUserId());
            remoteRequestParam.setDefaultFlag(requestParam.getIsDefault() ? 1 : 0);
            customerUserRemoteService.updateReceiveAddress(remoteRequestParam);
            updateReceiveAddressFlag = 1;
        } catch (Throwable ex) {
            log.error("调用用户服务修改收货地址失败", ex);
        }
        return updateReceiveAddressFlag;
    }
}
