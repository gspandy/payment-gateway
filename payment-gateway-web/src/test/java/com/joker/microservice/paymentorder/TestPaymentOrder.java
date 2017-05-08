package com.joker.microservice.paymentorder;

import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import org.junit.Test;

/**
 * Created by Joker on 2017/4/30.
 */
public class TestPaymentOrder {
    @Test
    public void testValidMethod() {

        System.out.println(PaymentOrder.isValidMethod(PaymentOrder.METHOD_ALIPAY_H5));

    }
}
