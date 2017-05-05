package com.globalroam.microservice.paymentgateway.service.impl;

import com.globalroam.microservice.paymentgateway.entity.PaymentOrder;
import com.globalroam.microservice.paymentgateway.service.PaymentNotificationService;
import com.joker.module.common.httpclient.HttpRequest;

import com.joker.module.common.httpclient.domain.Response;
import com.joker.module.common.request.HttpUtil;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/5/4.
 */
@Service
public class PaymentNotifucationServiceImpl implements PaymentNotificationService {

    private static Logger logger = LogManager.getLogger(PaymentNotifucationServiceImpl.class);
    @Override
    public void notification(PaymentOrder paymentOrder) {
        Header[] headers = {};
        String url = paymentOrder.getNotifyURL();
        HttpUtil.Host host = HttpUtil.parseURL(url);
        HttpRequest request = new HttpRequest(host.getHttpType(), headers, host.getAddress(),Integer.parseInt(host.getPort()));
        NameValuePair[] nameValuePairs = new NameValuePair[7];
        nameValuePairs[0] = new NameValuePair("id",paymentOrder.getId());
        nameValuePairs[1] = new NameValuePair("amount",paymentOrder.getAmount() + "");
        nameValuePairs[2] = new NameValuePair("status",paymentOrder.getStatus());
        nameValuePairs[3] = new NameValuePair("outTradeNo",paymentOrder.getOutTradeNo());
        nameValuePairs[4] = new NameValuePair("tradeNo",paymentOrder.getTradeNo());
        nameValuePairs[5] = new NameValuePair("title",paymentOrder.getTitle());
        nameValuePairs[6] = new NameValuePair("result","SUCCESS");
        Response<Object> objectResponse = request.post(host.getUri(), nameValuePairs);
        logger.info("notify result : " + objectResponse.getString());

    }


}
