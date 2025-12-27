package com.noinch.mall.biz.message.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "请求短信验证码")
public class PhoneSendCommand {

    @Schema(description = "手机号")
    private String phone;
}
