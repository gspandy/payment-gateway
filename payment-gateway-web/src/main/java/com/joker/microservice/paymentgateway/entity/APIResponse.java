package com.joker.microservice.paymentgateway.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @Author zhangjian
 * @Date 2017/3/21
 * @Copyright:
 * @Describe:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse {
    private String code;
    private String message;
    private String describe;

    public APIResponse() {
        this.code = "100000";
        this.message = "操作成功";
    }

    public APIResponse(String code, String message, String describe) {
        this.code = code;
        this.message = message;
        this.describe = describe;
    }

    public APIResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

}
