package com.joker.microservice.paymentgateway.exception.enums;

/**
 * Created by Administrator on 2017/5/9.
 */
public enum  PaymentOrderErrorInfo implements ErrorInfo{
    PARAMS_ERROR("100021","请求参数不正确");

    private String code;
    private String message;


    PaymentOrderErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
