package com.joker.module.payment.paypal.service.impl;

import com.joker.module.payment.paypal.config.PayPalConfig;
import com.joker.module.payment.paypal.domain.PaypalOrder;
import com.joker.module.payment.paypal.factory.PaypalFactory;
import com.joker.module.payment.paypal.service.PayPalService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjian on 2016/8/12.
 */
public class PaypalServiceImpl implements PayPalService {

    @Override
    public String createPayment(PaypalOrder paypalOrder, String returnUrl) {
        return basePayment(paypalOrder, returnUrl,PayPalConfig.PAYPAL_PAYMENT_METHOD);
    }

    @Override
    public String createCreditPayment(PaypalOrder paypalOrder, String returnUrl) {
        return basePayment(paypalOrder, returnUrl,PayPalConfig.CREDICARD_PAYMENT_METHOD);
    }

    private String basePayment(PaypalOrder paypalOrder, String returnUrl,String method) {
        Item item = PaypalFactory.createItem(paypalOrder.getName(), paypalOrder.getAmount(), "1", paypalOrder.getCurrency());

        Details details = PaypalFactory.createDetails("0", paypalOrder.getAmount(), "0");

        Amount amount = PaypalFactory.createAmount(paypalOrder.getAmount(), paypalOrder.getCurrency(), details);

        List<Item> items = new ArrayList<Item>();
        items.add(item);
        ItemList itemList = PaypalFactory.createItemList(items);

        Transaction transaction = PaypalFactory.createTransaction(itemList, amount, paypalOrder.getDescribe());
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = PaypalFactory.createPayer(method);

        RedirectUrls redirectUrls = PaypalFactory.createRedirectUrls(returnUrl, returnUrl);

        APIContext apiContext = PaypalFactory.createAPIContext(PayPalConfig.CLIENT_ID, PayPalConfig.CLIENT_SECRET, PayPalConfig.ENVIRONMENT);

        Payment createdPayment = PaypalFactory.createPayment("sale", payer, transactions, redirectUrls, apiContext);

        paypalOrder.setId(createdPayment.getId());

        return PaypalFactory.createAprrovelLink(createdPayment, apiContext);
    }


    @Override
    public boolean excutePayment(PaypalOrder paypalOrder) {

        Payment payment = new Payment();
        payment.setId(paypalOrder.getId());

        APIContext apiContext = PaypalFactory.createAPIContext(PayPalConfig.CLIENT_ID, PayPalConfig.CLIENT_SECRET, PayPalConfig.ENVIRONMENT);
        PaymentExecution paymentExecution = PaypalFactory.createPaymentExecution(paypalOrder.getPayerID());

        Payment executedPayment = PaypalFactory.createExcutedPayment(payment,apiContext,paymentExecution);

        return executedPayment == null?false:true;
    }

}
