package com.noinch.mall.biz.customer.user.application.service.handler.login;

import com.noinch.mall.biz.customer.user.application.req.UserLoginCommand;
import com.noinch.mall.biz.customer.user.application.resp.UserLoginRespDTO;
import com.noinch.mall.biz.customer.user.domain.aggregate.CustomerUser;
import com.noinch.mall.biz.customer.user.domain.repository.CustomerUserRepository;
import com.noinch.mall.springboot.starter.designpattern.strategy.AbstractExecuteStrategy;
import com.noinch.mall.springboot.starter.jwt.toolkit.TokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LoginByUsernameHandler implements AbstractExecuteStrategy<UserLoginCommand, UserLoginRespDTO> {

    private final CustomerUserRepository customerUserRepository;

    private final TokenUtil tokenUtil;

    @Override
    public String mark() {
        return "login_by_username";
    }

    @Override
    public UserLoginRespDTO executeResp(UserLoginCommand command) {
        CustomerUser customerUser = customerUserRepository.loginByUsername(command.getUsername(), command.getPassword());
        return UserLoginRespDTO.builder()
                .customerUserId(customerUser.getCustomerUserId())
                .account(customerUser.getAccount())
                .username(customerUser.getUsername())
                .accessToken(tokenUtil.generateAccessToken(customerUser.getUsername(), customerUser.getCustomerUserId().toString()))
                .build();
    }
}
