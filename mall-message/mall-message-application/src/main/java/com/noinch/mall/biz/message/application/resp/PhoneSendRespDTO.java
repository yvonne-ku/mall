package com.noinch.mall.biz.message.application.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "发送短信消息响应")
public class PhoneSendRespDTO {

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "短信发送状态")
    private boolean status;
}
