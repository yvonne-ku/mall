package com.noinch.mall.biz.message.application.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("请求短信验证码")
public class PhoneSendCommand {

    @ApiModelProperty("手机号")
    private String phone;
}
