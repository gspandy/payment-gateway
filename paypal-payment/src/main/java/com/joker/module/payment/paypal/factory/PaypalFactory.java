package com.joker.module.payment.paypal.factory;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangjian on 2016/7/12.
 */
public class PaypalFactory {

    /**
     *
     * @param name 商品名称
     * @param price 商品价格
     * @param quantity 商品数量
     * @param currency 价格单位
     * @return PayPal 付费项目
     */
    public static Item createItem(String name, String price, String quantity, String currency){

        try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            throw new RuntimeException("price must be double.");
        }

        try {
            Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new RuntimeException("quantity must be integer");
        }


        Item item = new Item();
        item.setName(name).setQuantity(quantity).setCurrency((currency == null || "".equals(currency)) ? "USD" :currency).setPrice(price);
        return item;
    }

    /**
     *
     * @param items PayPal 付费的项目集合
     * @return
     */
    public static ItemList createItemList(List<Item> items){

        if(items == null || items.size() == 0){
            throw new RuntimeException("items must be not empty.");
        }
        ItemList itemList = new ItemList();
        itemList.setItems(items);

        return itemList;
    }

    /**
     *
     * @param shipping 运费
     * @param subtotal 项目总费用
     * @param tax 稅费
     * @return PayPal明细
     */
    public static Details createDetails(String shipping, String subtotal, String tax){

        try {
            Double.parseDouble(subtotal);
        } catch (NumberFormatException e) {
            throw new RuntimeException("subtotal must be double.");
        }
        try {
            Double.parseDouble(shipping);
        } catch (NumberFormatException e) {
            throw new RuntimeException("shipping must be double.");
        }

        try {
            Double.parseDouble(tax);
        } catch (NumberFormatException e) {
            throw new RuntimeException("tax must be double.");
        }



        // ###Details
        // Let's you specify details of a payment amount.
        Details details = new Details();
        details.setShipping(shipping);
        details.setSubtotal(subtotal);
        details.setTax(tax);
        return details;
    }

    /**
     *
     * @param totalMoney 总金额
     * @param currency 货币类型
     * @param details PayPal 明细
     * @return PayPal 合计
     */
    public static Amount createAmount(String totalMoney, String currency, Details details){

        try {
            Double.valueOf(totalMoney);
        } catch (NumberFormatException e) {
            throw new RuntimeException("totalMoney must be double.");
        }

        if(details == null || "".equals(details)){
            throw new RuntimeException("details must be not empty.");
        }


        // ###Amount
        // Let's you specify a payment amount.
        Amount amount = new Amount();
        amount.setCurrency((currency == null || "".equals(currency)) ? "USD" :currency);//"USD"
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal(totalMoney);//7
        amount.setDetails(details);

        return amount;
    }


    /**
     *
     * @param itemList PayPal 付费项目集合
     * @param amount 总金额包含运费、税费以及商品费用
     * @param description 本次交易的     * @return
     */
    public static Transaction createTransaction(ItemList itemList,Amount amount,String description){

        if(itemList == null){
            throw new RuntimeException("itemList is empty");
        }

        if(amount == null){
            throw new RuntimeException("amount is empty");
        }

        // ###Transaction
        // A transaction defines the contract of a
        // payment - what is the payment for and who
        // is fulfilling it. Transaction is created with
        // a `Payee` and `Amount` types
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction
                .setDescription(description); //"This is the payment transaction description."
        transaction.setItemList(itemList);
        return transaction;
    }


    /**
     *
     * @param paymentMethod PayPal 支付方式参数
     * @return PayPal 买家
     */
    public static Payer createPayer(String paymentMethod){


        if(paymentMethod == null || "".equals(paymentMethod)){
            throw new RuntimeException("paymentMethod is empty,must be paypal or others");
        }
        // ###Payer
        // A resource representing a Payer that funds a payment
        // Payment Method
        // as 'paypal'
        Payer payer = new Payer();
        payer.setPaymentMethod(paymentMethod == null || "".equals(paymentMethod)? "paypal" :paymentMethod);
        return  payer;
    }


    /**
     *
     * @param cancelUrl 取消后返回URL加上guid 如:https://xxx.xxx.com/paymentwithpaypal?guid=xxx
     * @param returnUrl 返回URL 如:https://xxx.xxx.com/paymentwithpaypal?guid=xxx
     * @return PayPal 重定向需要的链接
     */
    public static RedirectUrls createRedirectUrls(String cancelUrl, String returnUrl){

        if(cancelUrl == null || "".equals(cancelUrl)){
            throw new RuntimeException("cancelUrl is empty");
        }

        if(returnUrl == null || "".equals(returnUrl)){
            throw new RuntimeException("returnUrl is empty");
        }


        // ###Redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(returnUrl);
        return redirectUrls;
    }


    /**
     *
     * @param clientID 调用PayPal api 提供的clientID
     * @param clientSecret 调用PayPal api 提供的clientSecret
     * @param mode 环境配置，测试环境：sandbox,正式环境：live
     * @return
     */
    public static APIContext createAPIContext(String clientID, String clientSecret, String mode){

        if(clientID == null || "".equals(mode)){
            throw new RuntimeException("clientID is empty");
        }

        if(clientSecret == null || "".equals(mode)){
            throw new RuntimeException("clientSecret is empty");
        }

        if(mode == null || "".equals(mode)){
            throw new RuntimeException("mode is empty");
        }

        // ### Api Context
        // Pass in a `ApiContext` object to authenticate
        // the call and to send a unique request id
        // (that ensures idempotency). The SDK generates
        // a request id if you do not pass one explicitly.
        APIContext apiContext = new APIContext(clientID, clientSecret, (mode == null || "".equals(mode))? "sandbox" :mode);
        return apiContext;
    }


    /**
     *
     * @param intent 交易目的
     * @param payer PayPal 买家目前只需要设置支付方式吧
     * @param transactions 本次的交易集合
     * @return PayPal Payment
     */
    public static Payment createPayment(String intent, Payer payer, List<Transaction> transactions, RedirectUrls redirectUrls, APIContext apiContext){

        if(payer == null){
            throw new RuntimeException("payer is empty");
        }

        if(transactions == null || transactions.size() == 0){
            throw new RuntimeException("transactions is empty");
        }

        if(redirectUrls == null){
            throw new RuntimeException("redirectUrls is empty");
        }

        if(apiContext == null){
            throw new RuntimeException("apiContext is empty");
        }


        // ###Payment
        // A Payment Resource; create one using
        // the above types and intent as 'sale'
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);
        return payment;
    }

    /**
     *
     * @param payment 本地PayPal 创建的订单
     * @param apiContext api认证容器
     * @return 返回跳转链接，用户点击跳转至PayPal
     */
    public static String createAprrovelLink(Payment payment, APIContext apiContext){

        if(payment == null){
            throw new RuntimeException("payment is empty");
        }

        if( apiContext == null){
            throw new RuntimeException("apiContext is empty");
        }

        Payment createdPayment = null;
        try {
            createdPayment = payment.create(apiContext);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }

        if(createdPayment == null){
            return null;
        }

        // ###Payment Approval Url
        Iterator<Links> links = createdPayment.getLinks().iterator();
        while (links.hasNext()) {
            Links link = links.next();
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                return link.getHref();
            }
        }

        return  null;
    }

    public static PaymentExecution createPaymentExecution(String payerId){

        if(payerId == null || "".equals(payerId)){
            throw new RuntimeException("payer id is empty");
        }

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return paymentExecution;
    }

    public static Payment createExcutedPayment(Payment payment,APIContext apiContext,PaymentExecution paymentExecution){

        if(payment == null){
            throw new RuntimeException("payment must be not empty");
        }
        if(apiContext == null){
            throw new RuntimeException("apiContext must be not empty");
        }
        if(paymentExecution == null){
            throw new RuntimeException("paymentExecution must be not empty");
        }


        try {
            payment.execute(apiContext, paymentExecution);
            payment.getLastRequest();
            payment.getLastResponse();
            return payment;
        } catch (PayPalRESTException e) {
            System.out.println("execute payment result:" + e.getMessage());
            return null;
        }
    }
}
