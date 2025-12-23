
package com.noinch.mall.biz.customer.user.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.noinch.mall.springboot.starter.common.toolkit.BeanUtil;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.customer.user.domain.mode.ReceiveAddress;
import com.noinch.mall.biz.customer.user.domain.repository.ReceiveAddressRepository;
import com.noinch.mall.biz.customer.user.infrastructure.dao.entity.ReceiveAddressDO;
import com.noinch.mall.biz.customer.user.infrastructure.dao.mapper.ReceiveAddressMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        return BeanUtil.convert(receiveAddressDOList, ReceiveAddress.class);
    }
    
    @Override
    public void saveReceiveAddress(ReceiveAddress receiveAddress) {
        ReceiveAddressDO receiveAddressDO = BeanUtil.convert(receiveAddress, ReceiveAddressDO.class);
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
        ReceiveAddressDO receiveAddressDO = BeanUtil.convert(receiveAddress, ReceiveAddressDO.class);
        receiveAddressMapper.update(receiveAddressDO, updateWrapper);
    }
}
