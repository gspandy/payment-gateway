package com.joker.module.payment.wechat.domain;

import java.io.Serializable;

/**
 * Created by zhangjian on 2016/8/19.
 */
public class RefundOrder implements Serializable{
    private String appid;//微信分配的公众账号ID（企业号corpid即为此appId）
    private String mchId;//微信支付分配的商户号
    private String deviceInfo;//终端设备号
    private String nonceStr;//随机字符串，不长于32位。推荐随机数生成算法
    private String sign;//签名，详见签名生成算法
    private String outTradeNo;//微信生成的订单号，在支付通知中有返回
    private String outRefundNo;//商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    private String totalFee;//订单总金额，单位为分，只能为整数，详见支付金额
    private String refundFee;// 	退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
    private String refundFeeType;//货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    private String opUserId;// 	操作员帐号, 默认为商户号

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundFeeType() {
        return refundFeeType;
    }

    public void setRefundFeeType(String refundFeeType) {
        this.refundFeeType = refundFeeType;
    }

    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }
}
