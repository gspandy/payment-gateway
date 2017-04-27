package com.globalroam.microservice.wallet.exception;

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
}
