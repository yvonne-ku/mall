package com.noinch.mall.biz.message.application.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("发送短信消息响应")
public class PhoneSendRespDTO {

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("短信发送状态")
    private boolean status;
}
