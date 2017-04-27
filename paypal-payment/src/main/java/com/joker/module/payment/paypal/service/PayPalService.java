package com.joker.module.payment.paypal.service;

import com.joker.module.payment.paypal.domain.PaypalOrder;

/**
 * Created by zhangjian on 2016/7/19.
 */
public interface PayPalService {
    public String createPayment(PaypalOrder paypaOder, String returnUrl);
    public boolean excutePayment(PaypalOrder topUpPayment);
    public String createCreditPayment(PaypalOrder paypalOrder, String returnUrl);
}
