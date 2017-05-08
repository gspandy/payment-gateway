package com.joker.microservice.paymentgateway.service;

import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import com.joker.microservice.paymentgateway.exception.DataNotFoundException;
import com.joker.microservice.paymentgateway.exception.ServiceException;

/**
 * Created by Joker on 2017/4/29.
 */
public interface PaymentOrderService {
    PaymentOrder getById(String id) throws ServiceException, DataNotFoundException;
    PaymentOrder update(PaymentOrder paymentOrder) throws ServiceException, DataNotFoundException;
    PaymentOrder add(PaymentOrder paymentOrder) throws ServiceException;

    PaymentOrder getByAmountAndOutTradeNo(String out_trade_no,double amount) throws ServiceException, DataNotFoundException;

    PaymentOrder getByOutTradeNo(String outTradeNo);
}
