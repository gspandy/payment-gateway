package com.globalroam.microservice.paymentgateway.entity;

import java.util.List;

/**
 * Created by Joker on 2017/4/30.
 */
public class PageResponse<T> extends APIResponse {

    private List<T> datas;
    private int offset;
    private int limit;
    private int total;

    public PageResponse(List<T> datas) {
        super();
        this.datas = datas;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
