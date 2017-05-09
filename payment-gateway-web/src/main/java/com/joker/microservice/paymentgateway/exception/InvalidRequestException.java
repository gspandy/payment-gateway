package com.joker.microservice.paymentgateway.exception;

import com.joker.microservice.paymentgateway.exception.enums.ErrorInfo;
import org.springframework.http.HttpStatus;

/**
 * @Author zhangjian
 * @Date 2017/2/13
 * @Copyright:
 * @Describe:
 */
public class InvalidRequestException extends BaseException {

    public InvalidRequestException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public InvalidRequestException(String errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidRequestException(String message, HttpStatus httpStatus, String code) {
        super(message, httpStatus, code);
    }
}
