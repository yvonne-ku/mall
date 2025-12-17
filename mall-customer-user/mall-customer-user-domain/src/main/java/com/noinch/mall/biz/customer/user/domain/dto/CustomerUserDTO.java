
package com.noinch.mall.biz.customer.user.domain.dto;

import lombok.Data;

/**
 * C 端用户
 */
@Data
public class CustomerUserDTO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 昵称
     */
    private String name;
    
    /**
     * 账号
     */
    private String account;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String mail;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 性别
     */
    private Integer gender;
    
    /**
     * 头像
     */
    private String avatar;
}
