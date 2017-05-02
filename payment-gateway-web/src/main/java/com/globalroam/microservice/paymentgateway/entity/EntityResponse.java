package com.globalroam.microservice.paymentgateway.entity;

/**
 * Created by Joker on 2017/4/30.
 */
public class EntityResponse<T> extends APIResponse{

    private T data;
    public EntityResponse(T data) {
        super();
        this.data = data;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
