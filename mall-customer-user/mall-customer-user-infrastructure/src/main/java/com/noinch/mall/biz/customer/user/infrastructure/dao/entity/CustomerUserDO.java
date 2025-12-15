
package com.noinch.mall.biz.customer.user.infrastructure.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.noinch.mall.springboot.starter.mybatisplus.BaseDO;

/**
 * C 端用户数据对象
 *
 */
@Data
@TableName("customer_user_test")
public class CustomerUserDO extends BaseDO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 昵称
     */
    private String username;
    
    /**
     * 账号
     */
    private String account;
    
    /**
     * 密码
     */
    private String password;
    
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
