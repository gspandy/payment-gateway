package com.joker.module.payment.wechat.domain;

import java.io.Serializable;

/**
 * Created by zhangjian on 2016/8/18.
 */
public class WechatPayResult implements Serializable{

    public static String SUCCESS = "SUCCESS";
    public static String FAIL = "FAIL";

    private String appid;// 微信分配的公众账号ID（企业号corpid即为此appId）
    private String attach;//商家数据包，原样返回
    private String bankType;//银行类型，采用字符串类型的银行标识，银行类型见银行列表
    private String feeType;//货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    private String isSubscribe;//
    private String mchId;// 微信支付分配的商户号
    private String nonceStr;//随机字符串，不长于32位
    private String openid;//用户在商户appid下的唯一标识
    private String outTradeNo;//商户系统的订单号，与请求一致。
    private String resultCode;// SUCCESS/FAIL
    private String returnCode;// SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    private String sign;//签名，详见签名算法
    private String timeEnd;//支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    private Integer totalFee;//订单总金额，单位为分
    private String tradeType;//JSAPI、NATIVE、APP
    private String transactionId;//微信支付订单号
    private String returnMsg;// 返回信息，如非空，为错误原因 签名失败 参数格式校验错误
    private String deviceInfo;//微信支付分配的终端设备号，
    private String errCode;//错误返回的信息描述
    private String errCodeDes;//错误返回的信息描述
    private Integer settlementTotalFee;//应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
    private Integer cashFee;//现金支付金额订单现金支付金额，详见支付金额
//    private String cashFeeType;/
    private Integer couponFee;//货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    private Integer couponCount;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public Integer getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public void setSettlementTotalFee(Integer settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
    }

    public Integer getCashFee() {
        return cashFee;
    }

    public void setCashFee(Integer cashFee) {
        this.cashFee = cashFee;
    }

    public Integer getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(Integer couponFee) {
        this.couponFee = couponFee;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }


    public boolean isSuccess(){
        return SUCCESS.equalsIgnoreCase(this.resultCode);
    }

    @Override
    public String toString() {
        return "WechatPayResult{" +
                "appid='" + appid + '\'' +
                ", attach='" + attach + '\'' +
                ", bankType='" + bankType + '\'' +
                ", feeType='" + feeType + '\'' +
                ", isSubscribe='" + isSubscribe + '\'' +
                ", mchId='" + mchId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", openid='" + openid + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", sign='" + sign + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", totalFee=" + totalFee +
                ", tradeType='" + tradeType + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errCodeDes='" + errCodeDes + '\'' +
                ", settlementTotalFee=" + settlementTotalFee +
                ", cashFee=" + cashFee +
                ", couponFee=" + couponFee +
                ", couponCount=" + couponCount +
                '}';
    }
}
