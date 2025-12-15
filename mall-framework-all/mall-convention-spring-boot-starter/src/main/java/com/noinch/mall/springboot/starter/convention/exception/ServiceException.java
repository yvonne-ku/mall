
package com.noinch.mall.springboot.starter.convention.exception;

import com.noinch.mall.springboot.starter.convention.errorcode.BaseErrorCode;
import com.noinch.mall.springboot.starter.convention.errorcode.IErrorCode;

/**
 * 服务端异常
 *
 */
public class ServiceException extends AbstractException {
    
    public ServiceException(String message) {
        this(message, null, BaseErrorCode.SERVICE_ERROR);
    }
    
    public ServiceException(IErrorCode errorCode) {
        this(null, errorCode);
    }
    
    public ServiceException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }
    
    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
    
    @Override
    public String toString() {
        return "ServiceException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
