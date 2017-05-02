package com.globalroam.microservice.paymentgateway.ability;

import com.globalroam.microservice.paymentgateway.entity.Card;

/**
 * @Author zhangjian
 * @Date 2017/3/25
 * @Copyright:
 * @Describe:
 */
public interface Transferable {
    boolean transfer(Card src, Card target) throws Exception;
}
