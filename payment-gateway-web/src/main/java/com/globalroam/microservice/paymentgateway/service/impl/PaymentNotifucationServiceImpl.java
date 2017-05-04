package com.globalroam.microservice.paymentgateway.service.impl;

import com.globalroam.microservice.paymentgateway.entity.PaymentOrder;
import com.globalroam.microservice.paymentgateway.service.PaymentNotificationService;
import com.joker.module.common.httpclient.HttpRequest;

/**
 * Created by Administrator on 2017/5/4.
 */
public class PaymentNotifucationServiceImpl implements PaymentNotificationService {

    @Override
    public boolean notification(PaymentOrder paymentOrder) {

        return true;
    }


}
