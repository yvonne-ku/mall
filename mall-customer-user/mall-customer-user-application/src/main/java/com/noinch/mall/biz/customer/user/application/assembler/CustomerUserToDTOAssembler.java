

package com.noinch.mall.biz.customer.user.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import com.noinch.mall.biz.customer.user.application.resp.UserLoginRespDTO;
import com.noinch.mall.biz.customer.user.application.resp.UserRegisterRespDTO;
import com.noinch.mall.biz.customer.user.domain.aggregate.CustomerUser;

/**
 * C 端用户 Entity 转换 DTO
 * */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerUserToDTOAssembler {
    
    /**
     * C 端用户 Entity 转换用户注册返回 DTO
     */
    @Mappings({
            @Mapping(source = "customerUser.username", target = "name"),
            @Mapping(source = "customerUser.phone", target = "phone"),
            @Mapping(source = "customerUser.account", target = "account")
    })
    UserRegisterRespDTO customerUserToUserRegisterRespDTO(CustomerUser customerUser);
    
    /**
     * C 端用户 Entity 转换用户登录返回 DTO
     */
    @Mappings({
            @Mapping(source = "customerUser.customerUserId", target = "customerUserId"),
            @Mapping(source = "customerUser.username", target = "username"),
            @Mapping(source = "customerUser.account", target = "account")
    })
    UserLoginRespDTO customerUserToUserLoginRespDTO(CustomerUser customerUser);
}
