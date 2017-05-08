package com.joker.microservice.paymentgateway.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by Joker on 2017/4/29.
 */
public class ServiceException extends BaseException {
    public ServiceException(String code, String message) {
        super(code, message);
    }

    public ServiceException(String message, HttpStatus httpStatus, String code) {
        super(message, httpStatus, code);
    }
}
