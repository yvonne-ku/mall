

package com.noinch.mall.biz.customer.user.infrastructure.repository;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.noinch.mall.springboot.starter.convention.exception.ClientException;
import lombok.AllArgsConstructor;
import com.noinch.mall.biz.customer.user.domain.aggregate.CustomerUser;
import com.noinch.mall.biz.customer.user.domain.event.OperationLogEvent;
import com.noinch.mall.biz.customer.user.domain.repository.CustomerUserRepository;
import com.noinch.mall.biz.customer.user.infrastructure.converter.CustomerUserConverter;
import com.noinch.mall.biz.customer.user.infrastructure.dao.entity.CustomerUserDO;
import com.noinch.mall.biz.customer.user.infrastructure.dao.entity.OperationLogDO;
import com.noinch.mall.biz.customer.user.infrastructure.dao.mapper.CustomerUserMapper;
import com.noinch.mall.biz.customer.user.infrastructure.dao.mapper.OperationLogMapper;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import org.springframework.stereotype.Repository;


/**
 * C 端用户仓储层实现
*
*/
@Repository
@AllArgsConstructor
public class CustomerUserRepositoryImpl implements CustomerUserRepository {
    
    private final CustomerUserMapper customerUserMapper;
    
    private final CustomerUserConverter customerUserConverter;

    private final OperationLogMapper operationLogMapper;
    
    @Override
    public CustomerUser find(Long customerUserId) {
        CustomerUserDO customerUserDO = customerUserMapper.selectById(customerUserId);
        return customerUserConverter.doToCustomerUser(customerUserDO);
    }
    
    @Override
    public CustomerUser findByMail(String mail) {
        LambdaQueryWrapper<CustomerUserDO> queryWrapper = Wrappers.lambdaQuery(CustomerUserDO.class).eq(CustomerUserDO::getMail, mail);
        CustomerUserDO customerUserDO = customerUserMapper.selectOne(queryWrapper);
        return customerUserConverter.doToCustomerUser(customerUserDO);
    }
    
    @Override
    public CustomerUser findByAccount(String account) {
        LambdaQueryWrapper<CustomerUserDO> queryWrapper = Wrappers.lambdaQuery(CustomerUserDO.class).eq(CustomerUserDO::getAccount, account);
        CustomerUserDO customerUserDO = customerUserMapper.selectOne(queryWrapper);
        return customerUserConverter.doToCustomerUser(customerUserDO);
    }
    
    @Override
    public CustomerUser register(CustomerUser customerUser) {
        CustomerUserDO customerUserDO = customerUserConverter.customerUserToDO(customerUser);
        int insert = customerUserMapper.insert(customerUserDO);
        if (insert < 1) {
            throw new ServiceException(BaseErrorCode.USER_REGISTER_ERROR);
        }
        Long customerUserId = customerUserDO.getId();
        return find(customerUserId);
    }
    
    @Override
    public void saveOperationLog(CustomerUser customerUser) {
        OperationLogEvent customerOperationLogEvent = customerUser.getOperationLogEvent();
        OperationLogDO operationLogDO = new OperationLogDO(JSON.toJSONString(customerOperationLogEvent.getAfterCustomerUser()));
        if (customerOperationLogEvent.getBeforeCustomerUser() != null) {
            operationLogDO.setBeforeContent(JSON.toJSONString(customerOperationLogEvent.getBeforeCustomerUser()));
        }
        operationLogDO.setCustomerUserId(customerUser.getCustomerUserId());
        operationLogMapper.insert(operationLogDO);
    }

    @Override
    public boolean findByUsername(String username) {
        LambdaQueryWrapper<CustomerUserDO> queryWrapper = Wrappers.lambdaQuery(CustomerUserDO.class).eq(CustomerUserDO::getUsername, username);
        CustomerUserDO customerUserDO = customerUserMapper.selectOne(queryWrapper);
        return customerUserDO != null;
    }

    @Override
    public boolean findByPhone(String phone) {
        LambdaQueryWrapper<CustomerUserDO> queryWrapper = Wrappers.lambdaQuery(CustomerUserDO.class).eq(CustomerUserDO::getPhone, phone);
        CustomerUserDO customerUserDO = customerUserMapper.selectOne(queryWrapper);
        return customerUserDO != null;
    }

    @Override
    public CustomerUser getByUsername(String username) {
        LambdaQueryWrapper<CustomerUserDO> queryWrapper = Wrappers.lambdaQuery(CustomerUserDO.class)
                .eq(CustomerUserDO::getUsername, username);
        CustomerUserDO customerUserDO = customerUserMapper.selectOne(queryWrapper);
        return customerUserConverter.doToCustomerUser(customerUserDO);
    }

    @Override
    public CustomerUser loginByUsername(String username, String password) {
        CustomerUser customerUser = this.getByUsername(username);
        // 用户名不存在
        if (customerUser == null) {
            throw new ClientException(BaseErrorCode.USER_NAME_NOT_EXIST_ERROR);
        }
        // 密码验证失败
        if (! customerUser.checkPassword(password)) {
            throw new ClientException(BaseErrorCode.PASSWORD_VERIFY_ERROR);
        }
        return customerUser;
    }
}
