package com.joker.microservice.paymentgateway.exception;

import com.joker.microservice.paymentgateway.exception.enums.ErrorInfo;
import org.springframework.http.HttpStatus;

/**
 * @Author zhangjian
 * @Date 2017/3/21
 * @Copyright:
 * @Describe:
 */
public class BaseException extends Exception {

    private HttpStatus httpStatus;
    private String code;
    private String message;
    private ErrorInfo errorInfo;

    public BaseException(ErrorInfo errorInfo) {
        this.code = errorInfo.getCode();
        this.message = errorInfo.getMessage();

    }

    public BaseException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(String message, HttpStatus httpStatus, String code) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
