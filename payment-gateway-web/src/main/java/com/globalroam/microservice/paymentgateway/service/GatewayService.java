package com.globalroam.microservice.paymentgateway.service;

import com.globalroam.microservice.paymentgateway.entity.PaymentOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Joker on 2017/5/2.
 */
public interface GatewayService {
    void wechatH5(PaymentOrder paymentOrder,  HttpServletRequest request, HttpServletResponse response);
    void wechatQRCode(PaymentOrder paymentOrder,  HttpServletRequest request, HttpServletResponse response);
    void wechatOpenH5(PaymentOrder paymentOrder, HttpServletRequest request, HttpServletResponse response);
    void alipayH5(PaymentOrder paymentOrder,  HttpServletRequest request, HttpServletResponse response);
    void alipayH5QRCode(PaymentOrder paymentOrder,  HttpServletRequest request, HttpServletResponse response);
}
