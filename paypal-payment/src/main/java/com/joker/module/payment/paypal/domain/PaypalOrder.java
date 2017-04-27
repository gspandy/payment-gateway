package com.joker.module.payment.paypal.domain;

/**
 * Created by zhangjian on 2016/7/14.
 */
public class PaypalOrder {

    private String id; //PalPal payment id
    private String userID; //用户的ID
    private String payerID; //用户PayPal 账户id
    private String productId; //用户付费的产品id
    private String orderId; //用户付费的gnum订单id


    private String name; //产品名称
    private String amount; //产品总金额
    private String currency; //产品货币类型
    private String describe; //描述信息


    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayerID() {
        return payerID;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "TopUpPayment{" +
                "id='" + id + '\'' +
                ", userID='" + userID + '\'' +
                ", payerID='" + payerID + '\'' +
                ", productId='" + productId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", describe='" + describe + '\'' +
                '}';
    }
}
