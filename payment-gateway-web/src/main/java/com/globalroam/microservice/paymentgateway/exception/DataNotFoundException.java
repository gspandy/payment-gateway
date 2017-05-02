package com.globalroam.microservice.paymentgateway.exception;

import org.springframework.http.HttpStatus;

/**
 * @Author zhangjian
 * @Date 2017/2/13
 * @Copyright:
 * @Describe:
 */
public class DataNotFoundException extends BaseException {

    public DataNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }

    public DataNotFoundException(String message, HttpStatus httpStatus, String code) {
        super(message, httpStatus, code);
    }
}
