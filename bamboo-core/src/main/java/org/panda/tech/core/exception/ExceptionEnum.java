package org.panda.tech.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.panda.tech.core.web.restful.Result;

@Getter
@AllArgsConstructor
public enum ExceptionEnum implements Result {
    /**
     * 系统异常
     */
    SYSTEM(ExceptionConstants.ERROR_SYSTEM_CODE, ExceptionConstants.EXCEPTION_SYSTEM),
    /**
     * 业务异常
     */
    BUSINESS(ExceptionConstants.EXCEPTION_BUSINESS_CODE, ExceptionConstants.EXCEPTION_BUSINESS),

    /**
     * 参数异常，适用于各种业务异常参数
     */
	PARAMETERS(ExceptionConstants.EXCEPTION_PARAMETERS_CODE, ExceptionConstants.EXCEPTION_PARAMETERS),
    /**
     * 必传参数异常
     */
    PARAMETERS_REQUIRED(ExceptionConstants.EXCEPTION_PARAMETERS_REQUIRED_CODE, ExceptionConstants.EXCEPTION_PARAMETERS_REQUIRED),

    /**
     * 未认证异常
     */
    UNAUTHORIZED(ExceptionConstants.UNAUTHORIZED_CODE, ExceptionConstants.UNAUTHORIZED),
    /**
     * Token认证校验异常
     */
    ILLEGAL_TOKEN(ExceptionConstants.ILLEGAL_TOKEN_CODE, ExceptionConstants.ILLEGAL_TOKEN),
    /**
     * Token过期失效
     */
    TOKEN_EXPIRED(ExceptionConstants.TOKEN_EXPIRED_CODE, ExceptionConstants.TOKEN_EXPIRED),
    /**
     * 没有操作权限异常
     */
    AUTH_NO_OPERA(ExceptionConstants.FORBIDDEN_CODE, ExceptionConstants.FORBIDDEN),

    /**
     * 拦截锁
     */
    INTERCEPT_LOCK(ExceptionConstants.INTERCEPT_LOCK_CODE, ExceptionConstants.INTERCEPT_LOCK),
    ;

    private int code;
    private String message;
    
}
