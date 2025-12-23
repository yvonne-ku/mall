package com.noinch.mall.biz.customer.user.application.convertor;

import com.noinch.mall.biz.customer.user.application.resp.UserLoginRespDTO;
import com.noinch.mall.biz.customer.user.application.resp.UserRegisterRespDTO;
import com.noinch.mall.biz.customer.user.domain.aggregate.CustomerUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-23T21:34:44+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class CustomerUserToDTOsConvertorImpl implements CustomerUserToDTOsConvertor {

    @Override
    public UserRegisterRespDTO customerUserToUserRegisterRespDTO(CustomerUser customerUser) {
        if ( customerUser == null ) {
            return null;
        }

        UserRegisterRespDTO.UserRegisterRespDTOBuilder userRegisterRespDTO = UserRegisterRespDTO.builder();

        userRegisterRespDTO.name( customerUser.getUsername() );
        userRegisterRespDTO.phone( customerUser.getPhone() );
        userRegisterRespDTO.account( customerUser.getAccount() );
        userRegisterRespDTO.customerUserId( customerUser.getCustomerUserId() );

        return userRegisterRespDTO.build();
    }

    @Override
    public UserLoginRespDTO customerUserToUserLoginRespDTO(CustomerUser customerUser) {
        if ( customerUser == null ) {
            return null;
        }

        UserLoginRespDTO.UserLoginRespDTOBuilder userLoginRespDTO = UserLoginRespDTO.builder();

        userLoginRespDTO.customerUserId( customerUser.getCustomerUserId() );
        userLoginRespDTO.username( customerUser.getUsername() );
        userLoginRespDTO.account( customerUser.getAccount() );

        return userLoginRespDTO.build();
    }
}
