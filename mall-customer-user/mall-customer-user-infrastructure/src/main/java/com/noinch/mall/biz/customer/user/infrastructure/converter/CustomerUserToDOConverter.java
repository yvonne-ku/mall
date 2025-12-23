

package com.noinch.mall.biz.customer.user.infrastructure.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import com.noinch.mall.biz.customer.user.domain.aggregate.CustomerUser;
import com.noinch.mall.biz.customer.user.infrastructure.dao.entity.CustomerUserDO;

/**
 * C 端用户 Entity 转换 DO
*
*/
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerUserToDOConverter {
    
    /**
     * C 端用户 Entity 转换 DO
     *
     * @param customerUser
     * @return
     */
    @Mappings({
            @Mapping(source = "customerUser.username", target = "username"),
            @Mapping(source = "customerUser.phone", target = "phone"),
            @Mapping(source = "customerUser.password.password", target = "password"),
            @Mapping(source = "customerUser.account", target = "account")
    })
    CustomerUserDO customerUserToDO(CustomerUser customerUser);
    
    /**
     * C 端用户 DO 转换 Entity
     *
     * @param customerUserDO
     * @return
     */
    @Mappings({
            @Mapping(source = "id", target = "customerUserId"),
            @Mapping(source = "username", target = "username.username"),
            @Mapping(source = "mail", target = "mail.mail"),
            @Mapping(source = "phone", target = "phone.phone"),
            @Mapping(source = "password", target = "password.password"),
            @Mapping(source = "account", target = "account.account")
    })
    CustomerUser doToCustomerUser(CustomerUserDO customerUserDO);
}
