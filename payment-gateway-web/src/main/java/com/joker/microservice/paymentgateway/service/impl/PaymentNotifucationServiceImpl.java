package com.joker.microservice.paymentgateway.service.impl;

import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import com.joker.microservice.paymentgateway.service.PaymentNotificationService;
import com.joker.module.common.httpclient.HttpRequest;

import com.joker.module.common.httpclient.domain.Response;
import com.joker.module.common.request.HttpUtil;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/5/4.
 */
@Service
public class PaymentNotifucationServiceImpl implements PaymentNotificationService {

    private static Logger logger = LogManager.getLogger(PaymentNotifucationServiceImpl.class);
    @Override
    public void notification(PaymentOrder paymentOrder) {
        Header[] headers = {new Header("Content-Type","application/x-www-form-urlencoded ")};
        String url = paymentOrder.getNotifyUrl();
        HttpUtil.Host host = HttpUtil.parseURL(url);
        HttpRequest request = new HttpRequest(host.getHttpType(), headers, host.getAddress(),Integer.parseInt(host.getPort()));

        StringBuilder params = new StringBuilder();
        params.append("id=");
        params.append(URLEncoder.encode(paymentOrder.getId()));
        params.append("&amount=");
        params.append(URLEncoder.encode(paymentOrder.getAmount()+""));
        params.append("&status=");
        params.append(URLEncoder.encode(paymentOrder.getStatus()));
        params.append("&outTradeNo=");
        params.append(URLEncoder.encode(paymentOrder.getOutTradeNo()));
        params.append("&tradeNo=");
        params.append(URLEncoder.encode(paymentOrder.getTradeNo()));
        params.append("&title=");
        params.append(URLEncoder.encode(paymentOrder.getTitle()));
        params.append("&custom=");
        params.append(URLEncoder.encode(paymentOrder.getCustom()));
        params.append("&result=");
        params.append(URLEncoder.encode("SUCCESS"));
        Response<Object> objectResponse = request.post(host.getUri(), params.toString());

        logger.info("notify result : " + objectResponse.getString());

    }


}
