package com.joker.microservice.paymentgateway.exception;

import org.springframework.http.HttpStatus;

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

    public AuthenticationException(String message, HttpStatus httpStatus, String code) {
        super(message, httpStatus, code);
    }
}
