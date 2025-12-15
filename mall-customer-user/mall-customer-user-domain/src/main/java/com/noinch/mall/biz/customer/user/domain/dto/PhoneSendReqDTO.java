package com.noinch.mall.biz.customer.user.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneSendReqDTO {

    private String phone;
}
