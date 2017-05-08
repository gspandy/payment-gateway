package com.joker.microservice.paymentgateway.ability;

import com.joker.microservice.paymentgateway.entity.Card;

/**
 * @Author zhangjian
 * @Date 2017/3/25
 * @Copyright:
 * @Describe:
 */
public interface Subtractable {
    boolean subtract(Card src, double amount) throws Exception;
}
