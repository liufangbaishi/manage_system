package com.cheng.manage.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author weicheng
 * @version v1.0.0
 * @description
 * @name LoginException
 * @date 2021/12/4 23:48
 */
public class LoginException extends AuthenticationException {
    public LoginException(String msg) {
        super(msg);
    }
}
