package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.Data;

@Data
public class UserRegisterAdapterRespDTO {

    private String id;

    private String username;

    private String email;

    private String phone;

    private Integer state;
}
