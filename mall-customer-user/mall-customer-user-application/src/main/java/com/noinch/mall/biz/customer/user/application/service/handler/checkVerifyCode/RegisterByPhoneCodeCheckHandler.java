
package com.noinch.mall.biz.customer.user.application.service.handler.checkVerifyCode;

import com.noinch.mall.biz.customer.user.domain.service.MessageRemoteService;
import com.noinch.mall.springboot.starter.convention.exception.ClientException;
import com.noinch.mall.springboot.starter.designpattern.strategy.AbstractExecuteStrategy;
import com.noinch.mall.biz.customer.user.application.req.UserRegisterCommand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



/**
 * C 端用户注册处理器
*
*/
@Component
@RequiredArgsConstructor
public class RegisterByPhoneCodeCheckHandler implements AbstractExecuteStrategy<UserRegisterCommand, Boolean> {

    private final MessageRemoteService messageRemoteService;

    @Override
    public String mark() {
        return "check_verify_code_register_by_phone";
    }

    @Override
    public Boolean executeResp(UserRegisterCommand requestParam) throws ClientException {
        return messageRemoteService.verifySms(requestParam.getPhone(), requestParam.getPhoneValidCode());
    }
}
