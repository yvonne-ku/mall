package com.noinch.mall.biz.bff.remote.resp;

import lombok.Data;

@Data
public class CheckPaymentStatusRespDTO {

    private String orderSn;

    private String status;
}
