package com.globalroam.microservice.wallet.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @Author zhangjian
 * @Date 2017/3/21
 * @Copyright:
 * @Describe:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
    private String code;
    private String message;
    private String describe;


    private T data;
    private List<T> datas;


    public APIResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public APIResponse(String code, String message, String describe) {
        this.code = code;
        this.message = message;
        this.describe = describe;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
