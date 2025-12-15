

package com.noinch.mall.biz.customer.user.domain.repository;

import com.noinch.mall.biz.customer.user.domain.aggregate.CustomerUser;

/**
 * C 端用户仓储层

 */
public interface CustomerUserRepository {
    
    /**
     * 根据 customerUserId 查询 C 端用户
     *
     * @param customerUserId
     * @return
     */
    CustomerUser find(Long customerUserId);
    
    /**
     * 根据 mail 查询 C 端用户
     *
     * @param mail
     * @return
     */
    CustomerUser findByMail(String mail);
    
    /**
     * 根据 account 查询 C 端用户
     *
     * @param account
     * @return
     */
    CustomerUser findByAccount(String account);
    
    /**
     * C 端用户注册
     *
     * @param customerUser
     * @return
     */
    CustomerUser register(CustomerUser customerUser);

    /**
     * 保存用户操作日志
     *
     * @param customerUser
     */
    void saveOperationLog(CustomerUser customerUser);

    boolean findByUsername(String username);

    boolean findByPhone(String phone);

    CustomerUser getByUsername(String username);

    CustomerUser loginByUsername(String username, String password);

}
