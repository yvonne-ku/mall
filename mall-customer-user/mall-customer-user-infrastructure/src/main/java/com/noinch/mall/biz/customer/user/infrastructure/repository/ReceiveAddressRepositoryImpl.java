
package com.noinch.mall.biz.customer.user.infrastructure.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.customer.user.domain.mode.ReceiveAddress;
import com.noinch.mall.biz.customer.user.domain.repository.ReceiveAddressRepository;
import com.noinch.mall.biz.customer.user.infrastructure.dao.entity.ReceiveAddressDO;
import com.noinch.mall.biz.customer.user.infrastructure.dao.mapper.ReceiveAddressMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户收货地址仓储层
 *
 */
@Repository
@AllArgsConstructor
public class ReceiveAddressRepositoryImpl implements ReceiveAddressRepository {
    
    private final ReceiveAddressMapper receiveAddressMapper;
    
    @Override
    public List<ReceiveAddress> listReceiveAddressByCustomerUserId(String customerUserId) {
        List<ReceiveAddressDO> receiveAddressDOList = receiveAddressMapper.selectList(Wrappers.lambdaQuery(ReceiveAddressDO.class).eq(ReceiveAddressDO::getCustomerUserId, customerUserId));
        return receiveAddressDOList.stream().map(each -> {
            ReceiveAddress receiveAddress = new ReceiveAddress();
            BeanUtil.copyProperties(each, receiveAddress, CopyOptions.create().setIgnoreNullValue(true));
            return receiveAddress;
        }).collect(Collectors.toList());
    }
    
    @Override
    public void saveReceiveAddress(ReceiveAddress receiveAddress) {
        ReceiveAddressDO receiveAddressDO = new ReceiveAddressDO();
        BeanUtil.copyProperties(receiveAddress, receiveAddressDO, CopyOptions.create().setIgnoreNullValue(true));
        receiveAddressMapper.insert(receiveAddressDO);
    }
    
    @Override
    public void removeReceiveAddress(String customerUserId, String receiveAddressId) {
        LambdaUpdateWrapper<ReceiveAddressDO> updateWrapper = Wrappers.lambdaUpdate(ReceiveAddressDO.class)
                .eq(ReceiveAddressDO::getCustomerUserId, customerUserId)
                .eq(ReceiveAddressDO::getId, receiveAddressId);
        receiveAddressMapper.delete(updateWrapper);
    }
    
    @Override
    public void updateReceiveAddress(ReceiveAddress receiveAddress) {
        LambdaUpdateWrapper<ReceiveAddressDO> updateWrapper = Wrappers.lambdaUpdate(ReceiveAddressDO.class)
                .eq(ReceiveAddressDO::getCustomerUserId, receiveAddress.getCustomerUserId())
                .eq(ReceiveAddressDO::getId, receiveAddress.getId());
        ReceiveAddressDO receiveAddressDO = new ReceiveAddressDO();
        BeanUtil.copyProperties(receiveAddress, receiveAddressDO, CopyOptions.create().setIgnoreNullValue(true));
        receiveAddressMapper.update(receiveAddressDO, updateWrapper);
    }
}
