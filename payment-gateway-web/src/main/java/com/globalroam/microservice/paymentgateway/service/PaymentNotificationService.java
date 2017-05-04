package com.globalroam.microservice.paymentgateway.service;

import com.globalroam.microservice.paymentgateway.entity.PaymentOrder;

/**
 * Created by Administrator on 2017/5/4.
 */
public interface PaymentNotificationService
{
     void notification(PaymentOrder paymentOrder);
}
