package com.globalroam.microservice.wallet.exception;

/**
 * @Author zhangjian
 * @Date 2017/2/13
 * @Copyright:
 * @Describe:
 */
public class AuthenticationException extends BaseException {

    public AuthenticationException(String errorCode, String message) {
        super(errorCode, message);
    }
}
