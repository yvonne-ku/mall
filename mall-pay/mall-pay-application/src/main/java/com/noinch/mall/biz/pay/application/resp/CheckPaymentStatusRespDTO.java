package com.noinch.mall.biz.pay.application.resp;

import lombok.Data;

@Data
public class CheckPaymentStatusRespDTO {

    private String orderSn;

    private String status;
}
