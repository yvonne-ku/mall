

package com.noinch.mall.biz.customer.user.domain.dp;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.exception.ClientException;

/**
 * C 端用户注册手机号
 */
@Data
public class CustomerUserPhone {
    
    /**
     * 注册手机号
     */
    private final String phone;
    
    public CustomerUserPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            throw new ClientException("手机号不能为空");
        } else if (!PhoneUtil.isMobile(phone)) {
            throw new ClientException(BaseErrorCode.PHONE_VERIFY_ERROR);
        }
        this.phone = phone;
    }
}
