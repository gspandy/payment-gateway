package com.globalroam.microservice.wallet.exception;

/**
 * @Author zhangjian
 * @Date 2017/3/21
 * @Copyright:
 * @Describe:
 */
public class BaseException extends Exception {

    private String code;
    private String message;

    public BaseException(String code, String message) {
        this.code = code;
        this.message = message;
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
