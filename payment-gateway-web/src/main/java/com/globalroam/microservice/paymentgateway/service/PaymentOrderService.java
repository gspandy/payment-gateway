package com.globalroam.microservice.paymentgateway.service;

import com.globalroam.microservice.paymentgateway.entity.PaymentOrder;
import com.globalroam.microservice.paymentgateway.exception.DataNotFoundException;
import com.globalroam.microservice.paymentgateway.exception.ServiceException;

/**
 * Created by Joker on 2017/4/29.
 */
public interface PaymentOrderService {
    PaymentOrder getById(String id) throws ServiceException, DataNotFoundException;
    PaymentOrder update(PaymentOrder paymentOrder) throws ServiceException, DataNotFoundException;
    PaymentOrder add(PaymentOrder paymentOrder) throws ServiceException;

    PaymentOrder getByOutTradeNo(String out_trade_no) throws ServiceException, DataNotFoundException;
}
