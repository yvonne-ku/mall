package com.noinch.mall.biz.message.interfaces.controller;

import com.noinch.mall.biz.message.application.req.PhoneSendCommand;
import com.noinch.mall.biz.message.application.req.PhoneVerifyCommand;
import com.noinch.mall.biz.message.application.resp.PhoneSendRespDTO;
import com.noinch.mall.biz.message.application.service.MessageService;
import com.noinch.mall.springboot.starter.convention.result.Result;
import com.noinch.mall.springboot.starter.web.Results;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "消息发送")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/api/message/send/phone")
    @Operation(summary = "发送短信验证码")
    public Result<PhoneSendRespDTO> sendPhoneMessage(@RequestBody @Valid PhoneSendCommand phoneSendCommand) {
        PhoneSendRespDTO result = messageService.phoneMessageSend(phoneSendCommand);
        return Results.success(result);
    }

    @PostMapping("/api/message/verify/phone")
    @Operation(summary = "验证短信验证码")
    public Result<Boolean> verifyPhoneMessage(@RequestBody @Valid PhoneVerifyCommand phoneVerifyCommand) {
        return Results.success(messageService.phoneMessageVerify(phoneVerifyCommand));
    }
}