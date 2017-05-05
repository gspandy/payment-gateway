package com.globalroam.microservice.paymentgateway.entity;

import com.globalroam.microservice.paymentgateway.common.CommonEntity;

import javax.persistence.Table;

/**
 * Created by Joker on 2017/4/29.
 */
@Table(name = "pg_payment_order")
public class PaymentOrder extends CommonEntity {
    private double amount;
    private String method;
    private String title;
    private String status;
    private String outTradeNo;
    private String tradeNo;

    private String returnURL;
    private String notifyURL;

    public PaymentOrder() {

    }

    public PaymentOrder(double amount, String title, String method, String status) {
        this.amount = amount;
        this.title = title;
        this.method = method;
        this.status = status;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    //支付单创建状态
    public static String STATUS_CREATE = "CREATE";
    //支付单正在充
    public static String STATUS_PAY = "PAY";
    //支付单调用接口支付成功
    public static String STATUS_SUCCESS = "SUCCESS";
    //支付单完成支付流程
    public static String STATUS_COMPLETE = "COMPLETE";
    //支付宝返回支付失败
    public static String STATUS_FAIL = "FAIL";

    //支付方式 -- 支付宝支付H5
    public static final String METHOD_ALIPAY_H5 = "ALIPAY_H5";
    //支付方式 -- 支付宝支付二维码
    public static final String METHOD_ALIPAY_QRCODE = "ALIPAY_QRCODE";
    //支付方式 -- 微信H5
    public static final String METHOD_WECHAT_H5 = "WECHAT_H5";
    //支付方式 -- 微信二维码
    public static final String METHOD_WECHAT_QRCODE = "WECHAT_QRCODE";
    //支付方式 -- 微信公众号H5
    public static final String METHOD_WECHAT_OPEN_H5 = "WECHAT_OPEN_H5";

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public static boolean isValidMethod(String method) {
        return METHOD_ALIPAY_H5.equalsIgnoreCase(method) || METHOD_ALIPAY_QRCODE.equalsIgnoreCase(method) || METHOD_WECHAT_H5.equalsIgnoreCase(method) || METHOD_WECHAT_QRCODE.equalsIgnoreCase(method) || METHOD_WECHAT_OPEN_H5.equalsIgnoreCase(method);
    }

}
