package com.globalroam.microservice.paymentorder;

import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import com.joker.microservice.paymentgateway.service.PaymentNotificationService;
import com.joker.microservice.paymentgateway.service.PaymentOrderService;
import com.joker.microservice.paymentgateway.service.impl.PaymentNotifucationServiceImpl;
import com.joker.microservice.paymentgateway.service.impl.PaymentOrderServiceImpl;
import com.joker.module.common.httpclient.HttpRequest;
import com.joker.module.common.httpclient.domain.Response;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/8.
 */
public class TestNotify {

    @Test
    public void testNotify() {

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setId("76adb62e-33a8-11e7-bf30-00ac8fea48d7");
        paymentOrder.setAmount(3.0);
        paymentOrder.setCreateBy("aaaa");
        paymentOrder.setStatus(PaymentOrder.STATUS_SUCCESS);
        paymentOrder.setTitle("测试");
        paymentOrder.setMethod("ALIPAY_H5");
        paymentOrder.setReturnUrl("http://joker.iask.in/public/v1/test");
        paymentOrder.setNotifyUrl("http://joker.iask.in/public/v1/test");
        paymentOrder.setCustom("{\"aaa\":\"vvv\"}");
        paymentOrder.setOutTradeNo("20170508201122212");

        PaymentNotificationService paymentNotificationService = new PaymentNotifucationServiceImpl();
        paymentNotificationService.notification(paymentOrder);

    }


    @Test
    public void testHttpClient() {

    }
}
