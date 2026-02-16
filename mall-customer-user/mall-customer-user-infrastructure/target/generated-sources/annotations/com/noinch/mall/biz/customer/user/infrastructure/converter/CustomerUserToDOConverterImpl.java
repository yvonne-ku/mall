package com.noinch.mall.biz.customer.user.infrastructure.converter;

import com.noinch.mall.biz.customer.user.domain.aggregate.CustomerUser;
import com.noinch.mall.biz.customer.user.domain.dp.CustomerUserAccount;
import com.noinch.mall.biz.customer.user.domain.dp.CustomerUserMail;
import com.noinch.mall.biz.customer.user.domain.dp.CustomerUserName;
import com.noinch.mall.biz.customer.user.domain.dp.CustomerUserPassword;
import com.noinch.mall.biz.customer.user.domain.dp.CustomerUserPhone;
import com.noinch.mall.biz.customer.user.infrastructure.dao.entity.CustomerUserDO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-17T03:05:26+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class CustomerUserToDOConverterImpl implements CustomerUserToDOConverter {

    @Override
    public CustomerUserDO customerUserToDO(CustomerUser customerUser) {
        if ( customerUser == null ) {
            return null;
        }

        CustomerUserDO customerUserDO = new CustomerUserDO();

        customerUserDO.setUsername( customerUser.getUsername() );
        customerUserDO.setPhone( customerUser.getPhone() );
        customerUserDO.setPassword( customerUserPasswordPassword( customerUser ) );
        customerUserDO.setAccount( customerUser.getAccount() );
        customerUserDO.setMail( customerUser.getMail() );

        return customerUserDO;
    }

    @Override
    public CustomerUser doToCustomerUser(CustomerUserDO customerUserDO) {
        if ( customerUserDO == null ) {
            return null;
        }

        CustomerUser.CustomerUserBuilder customerUser = CustomerUser.builder();

        customerUser.username( customerUserDOToCustomerUserName( customerUserDO ) );
        customerUser.mail( customerUserDOToCustomerUserMail( customerUserDO ) );
        customerUser.phone( customerUserDOToCustomerUserPhone( customerUserDO ) );
        customerUser.password( customerUserDOToCustomerUserPassword( customerUserDO ) );
        customerUser.account( customerUserDOToCustomerUserAccount( customerUserDO ) );
        customerUser.customerUserId( customerUserDO.getId() );

        return customerUser.build();
    }

    private String customerUserPasswordPassword(CustomerUser customerUser) {
        if ( customerUser == null ) {
            return null;
        }
        CustomerUserPassword password = customerUser.getPassword();
        if ( password == null ) {
            return null;
        }
        String password1 = password.getPassword();
        if ( password1 == null ) {
            return null;
        }
        return password1;
    }

    protected CustomerUserName customerUserDOToCustomerUserName(CustomerUserDO customerUserDO) {
        if ( customerUserDO == null ) {
            return null;
        }

        String username = null;

        username = customerUserDO.getUsername();

        CustomerUserName customerUserName = new CustomerUserName( username );

        return customerUserName;
    }

    protected CustomerUserMail customerUserDOToCustomerUserMail(CustomerUserDO customerUserDO) {
        if ( customerUserDO == null ) {
            return null;
        }

        String mail = null;

        mail = customerUserDO.getMail();

        CustomerUserMail customerUserMail = new CustomerUserMail( mail );

        return customerUserMail;
    }

    protected CustomerUserPhone customerUserDOToCustomerUserPhone(CustomerUserDO customerUserDO) {
        if ( customerUserDO == null ) {
            return null;
        }

        String phone = null;

        phone = customerUserDO.getPhone();

        CustomerUserPhone customerUserPhone = new CustomerUserPhone( phone );

        return customerUserPhone;
    }

    protected CustomerUserPassword customerUserDOToCustomerUserPassword(CustomerUserDO customerUserDO) {
        if ( customerUserDO == null ) {
            return null;
        }

        String password = null;

        password = customerUserDO.getPassword();

        CustomerUserPassword customerUserPassword = new CustomerUserPassword( password );

        return customerUserPassword;
    }

    protected CustomerUserAccount customerUserDOToCustomerUserAccount(CustomerUserDO customerUserDO) {
        if ( customerUserDO == null ) {
            return null;
        }

        String account = null;

        account = customerUserDO.getAccount();

        CustomerUserAccount customerUserAccount = new CustomerUserAccount( account );

        return customerUserAccount;
    }
}
