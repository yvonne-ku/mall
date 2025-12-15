package com.noinch.mall.biz.customer.user.application.service.handler.sendVerifyCode;

import com.noinch.mall.biz.customer.user.application.req.UserVerifyCodeCommand;
import com.noinch.mall.biz.customer.user.domain.service.MessageRemoteService;
import com.noinch.mall.springboot.starter.designpattern.strategy.AbstractExecuteStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterByPhoneCodeSendHandler implements AbstractExecuteStrategy<UserVerifyCodeCommand, Void> {

    private final MessageRemoteService messageRemoteService;

    @Override
    public String mark() {
        return "send_verify_code_register_by_phone";
    }

    @Override
    public void execute(UserVerifyCodeCommand command) {
        messageRemoteService.sendSms(command.getReceiver());
    }
}
