
package com.noinch.mall.biz.customer.user.application.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.customer.user.application.req.ReceiveAddressSaveCommand;
import com.noinch.mall.biz.customer.user.application.req.ReceiveAddressUpdateCommand;
import com.noinch.mall.biz.customer.user.application.resp.ReceiveAddressRespDTO;
import com.noinch.mall.biz.customer.user.application.service.ReceiveAddressService;
import com.noinch.mall.biz.customer.user.domain.mode.ReceiveAddress;
import com.noinch.mall.biz.customer.user.domain.repository.ReceiveAddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户收货地址
*
*/
@Service
@AllArgsConstructor
public class ReceiveAddressServiceImpl implements ReceiveAddressService {
    
    private final ReceiveAddressRepository receiveAddressRepository;

    @Override
    public List<ReceiveAddressRespDTO> listReceiveAddressByCustomerUserId(String customerUserId) {
        List<ReceiveAddress> receiveAddresses = receiveAddressRepository.listReceiveAddressByCustomerUserId(customerUserId);
        return receiveAddresses.stream().map(address -> {
            ReceiveAddressRespDTO dto = new ReceiveAddressRespDTO();
            BeanUtil.copyProperties(address, dto, CopyOptions.create().setIgnoreNullValue(true));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void saveReceiveAddress(ReceiveAddressSaveCommand requestParam) {
        ReceiveAddress receiveAddress = new ReceiveAddress();
        BeanUtil.copyProperties(requestParam, receiveAddress, CopyOptions.create().setIgnoreNullValue(true));
        receiveAddressRepository.saveReceiveAddress(receiveAddress);
    }
    
    @Override
    public void removeReceiveAddress(String customerUserId, String receiveAddressId) {
        receiveAddressRepository.removeReceiveAddress(customerUserId, receiveAddressId);
    }
    
    @Override
    public void updateReceiveAddress(ReceiveAddressUpdateCommand requestParam) {
        ReceiveAddress receiveAddress = new ReceiveAddress();
        BeanUtil.copyProperties(requestParam, receiveAddress, CopyOptions.create().setIgnoreNullValue(true));
        receiveAddressRepository.updateReceiveAddress(receiveAddress);
    }
}
