package com.joker.module.payment.wechat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.joker.module.payment.wechat.config.WechatPaymentConfig;
import com.joker.module.payment.wechat.domain.*;
import com.joker.module.payment.wechat.exception.LongURLException;
import com.joker.module.payment.wechat.exception.WechatOrderException;
import com.joker.module.payment.wechat.exception.WechatServiceException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangjian on 2016/7/31.
 */
public class WechatPaymentUtil {

    public static String CHARACTER_ENCODING_UTF8 = "UTF-8";

    /**
     * 将WechatOrder转换成xml参数并且根据ASCII排序，加密生成SIGN
     * @param wechatOrder
     * @param characterEncoding
     * @return
     * @throws WechatOrderException
     */
    public static String generateSortedXMLFromWechatOrder(WechatOrder wechatOrder, String characterEncoding) throws WechatOrderException {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        checkOrderRequiredParams(wechatOrder);
        addOrderParamsToSortedMap(wechatOrder, parameters);
        String mySign = createSign(characterEncoding, parameters, wechatOrder.getKey());
        parameters.put("sign", mySign);

        return CommonUtil.converterMapToXml(parameters);
    }

    /**
     * 将wechatOrder 中的参数放入排序map
     * @param wechatOrder
     * @param parameters
     */
    private static void addOrderParamsToSortedMap(WechatOrder wechatOrder, SortedMap<Object, Object> parameters) {
        //将必传参数添加至排序map
        parameters.put("appid", wechatOrder.getAppid());

        parameters.put("body", wechatOrder.getBody());

        parameters.put("device_info", wechatOrder.getDeviceInfo());
        parameters.put("mch_id", wechatOrder.getMchId());
        parameters.put("nonce_str", wechatOrder.getNonceStr());
        parameters.put("notify_url", wechatOrder.getNotifyUrl());

        parameters.put("out_trade_no", wechatOrder.getOutTradeNo());
        parameters.put("spbill_create_ip", wechatOrder.getSpbillCreateIp());
        parameters.put("total_fee", wechatOrder.getTotalFee());
        parameters.put("trade_type", wechatOrder.getTradeType());


        //不同条件下必传参数加入排序map
        if (WechatOrder.TRADE_TYPE_JSAPI.equals(wechatOrder.getTradeType()))
            parameters.put("openid", wechatOrder.getOpenid());
        if (WechatOrder.TRADE_TYPE_NATIVE.equals(wechatOrder.getTradeType()))
            parameters.put("product_id", wechatOrder.getProductId());


        //可选参数非空加入排序map
        if (!isEmpty(wechatOrder.getAttach()))
            parameters.put("attach", wechatOrder.getAttach());

        if (!isEmpty(wechatOrder.getDetail()))
            parameters.put("detail", wechatOrder.getDetail());

        if (!isEmpty(wechatOrder.getFeeType()))
            parameters.put("fee_type", wechatOrder.getFeeType());

        if (!isEmpty(wechatOrder.getTimeStart()))
            parameters.put("time_start", wechatOrder.getTimeStart());

        if (!isEmpty(wechatOrder.getTimeExpire()))
            parameters.put("time_expire", wechatOrder.getTimeExpire());

        if (!isEmpty(wechatOrder.getGoodsTag()))
            parameters.put("goods_tag", wechatOrder.getGoodsTag());

        if (!isEmpty(wechatOrder.getLimitPay()))
            parameters.put("limit_pay", wechatOrder.getLimitPay());
    }

    /**
     * 检测统一下单必填参数
     * @param wechatOrder
     * @throws WechatOrderException
     */
    private static void checkOrderRequiredParams(WechatOrder wechatOrder) throws WechatOrderException {

        if (isEmpty(wechatOrder.getKey())) {
            throw new WechatOrderException("API key is empty.");
        }

        if (isEmpty(wechatOrder.getAppid())) {
            throw new WechatOrderException("appid is empty.");
        }

        if (isEmpty(wechatOrder.getDeviceInfo())) {
            throw new WechatOrderException("deviceInfo is empty.");
        }

        if (isEmpty(wechatOrder.getMchId())) {
            throw new WechatOrderException("mchId is empty.");
        }

        if (isEmpty(wechatOrder.getNonceStr())) {
            throw new WechatOrderException("nonceStr is empty.");
        }

        if (isEmpty(wechatOrder.getBody())) {
            throw new WechatOrderException("body is empty.");
        }

        if (isEmpty(wechatOrder.getNotifyUrl())) {
            throw new WechatOrderException("notifyUrl is empty.");
        }

        if (isEmpty(wechatOrder.getOutTradeNo())) {
            throw new WechatOrderException("outTradeNo is empty.");
        }

        if (isEmpty(wechatOrder.getSpbillCreateIp())) {
            throw new WechatOrderException("spbillCreateIp is empty.");
        }

        if (wechatOrder.getTotalFee() <= 0) {
            throw new WechatOrderException("totalFee is empty.");
        }

        if (isEmpty(wechatOrder.getTradeType())) {
            throw new WechatOrderException("tradeType is empty.");
        }

        if (WechatOrder.TRADE_TYPE_JSAPI.equals(wechatOrder.getTradeType()) && isEmpty(wechatOrder.getOpenid())) {
            throw new WechatOrderException("tradeType is " + WechatOrder.TRADE_TYPE_JSAPI + ", but openid is empty.");
        }

        if (WechatOrder.TRADE_TYPE_NATIVE.equals(wechatOrder.getTradeType()) && isEmpty(wechatOrder.getProductId())) {
            throw new WechatOrderException("tradeType is " + WechatOrder.TRADE_TYPE_NATIVE + ", but productId is empty.");
        }
    }

    /**
     * 微信支付签名算法sign
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        System.out.println(sb.toString());
        String sign = MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        System.out.println("sign generated : " + sign);
        return sign;
    }

    public static String getParamsStr(Map<String, String> map) {

        String params = "";
        if (map == null || map.size() == 0) {
            return params;
        }

        Iterator<String> iterator = map.keySet().iterator();

        try {
            int i = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                params += key + "=" + URLEncoder.encode(map.get(key), "UTF-8");
                if (i + 1 < map.size()) {
                    params += "&";
                }
                i++;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return params;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static boolean isEmpty(String string) {

        return null == string || "".equals(string);

    }


    public static  String generateOuteTradeNo(String prefix){
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }


    public static String generateH5PayJson(WechatPrePayOrder prePayOrder){
        SortedMap<Object, Object> params = new TreeMap<Object, Object>();

        String uuid = UUID.randomUUID().toString().replace("-", "");
        long timestamp = System.currentTimeMillis();
        params.put("appId", WechatPaymentConfig.APPID);
        params.put("timeStamp", timestamp + "");
        params.put("nonceStr", uuid);
        params.put("package", "prepay_id=" + prePayOrder.getPrepayId());
        params.put("signType", "MD5");

        String sgn = WechatPaymentUtil.createSign("UTF-8", params, WechatPaymentConfig.KEY);
        params.put("paySign",sgn);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonResult = null;
        try {
            jsonResult = objectMapper.writeValueAsString(params);
            return jsonResult;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return jsonResult;
        }


    }

    //将微信充值结果xml数据转换为对象转换
    public static WechatPayResult parseNotifyXmlData(String xmlData){
        XmlMapper mapper = new XmlMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        try {
            WechatPayResult wechatPayResult = mapper.readValue(xmlData,WechatPayResult.class);
            return  wechatPayResult;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param params 签名参数
     * @param sign 签名值
     * @param key apikey
     * @return
     */
    public static boolean checkSign(SortedMap<Object,Object> params,String sign,String key){
        if(sign.equals(createSign("UTF-8",params,key))){
            return true;
        }
        return false;
    }


    public static boolean checkNotifyUrlSign(WechatPayResult wechatPayResult,String key){
        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
        params.put("appid", wechatPayResult.getAppid());
        params.put("bank_type", wechatPayResult.getBankType());
        params.put("cash_fee", wechatPayResult.getCashFee());
        params.put("device_info", wechatPayResult.getDeviceInfo());
        params.put("fee_type", wechatPayResult.getFeeType());
        params.put("is_subscribe", wechatPayResult.getIsSubscribe());
        params.put("mch_id", wechatPayResult.getMchId());
        params.put("nonce_str", wechatPayResult.getNonceStr());
        params.put("openid", wechatPayResult.getOpenid());
        params.put("out_trade_no", wechatPayResult.getOutTradeNo());
        params.put("result_code", wechatPayResult.getResultCode());
        params.put("return_code", wechatPayResult.getReturnCode());
        params.put("time_end", wechatPayResult.getTimeEnd());
        params.put("total_fee", wechatPayResult.getTotalFee());
        params.put("trade_type", wechatPayResult.getTradeType());
        params.put("transaction_id", wechatPayResult.getTransactionId());
        return checkSign(params,wechatPayResult.getSign(),key);
    }



    
    public static String generateSortedXMLFromLongURL(LongURL longURL1) throws LongURLException {
        checkLongURLRequiredParams(longURL1);
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        addLongURLParamsToSortedMap(longURL1, parameters);
        String mySign = createSign(CHARACTER_ENCODING_UTF8, parameters, WechatPaymentConfig.KEY);
        parameters.put("sign", mySign);
        return CommonUtil.converterMapToXml(parameters);
    }

    private static void addLongURLParamsToSortedMap(LongURL longURL1, SortedMap<Object, Object> parameters) {
        parameters.put("appid",longURL1.getAppid());
        parameters.put("mch_id",longURL1.getMchId());
        parameters.put("long_url",longURL1.getLongUrl());
        parameters.put("nonce_str",longURL1.getNonceStr());
    }

    private static void checkLongURLRequiredParams(LongURL longURL1) throws LongURLException {
        if(longURL1.getAppid() == null){
            throw  new LongURLException("appid 必填参数");
        }

        if(longURL1.getMchId() == null){
            throw new LongURLException("mch_id 必填参数");
        }

        if(longURL1.getLongUrl() == null){
            throw new LongURLException("long_url 必填参数 ");
        }

        if(longURL1.getNonceStr() == null){
            throw new LongURLException("nonce_str 必填参数 ");
        }
    }

    public static String generateSortedXMLFromQueryOrderParams(QueryOrderParam queryOrderParam, String characterEncodingUtf8) throws WechatServiceException {

        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        checkQueryOrderRequiredParams(queryOrderParam);
        addQueryOrderParamsToSortedMap(queryOrderParam, parameters);
        String mySign = createSign("UTF-8", parameters, queryOrderParam.getKey());
        parameters.put("sign", mySign);

        return CommonUtil.converterMapToXml(parameters);
    }

    private static void addQueryOrderParamsToSortedMap(QueryOrderParam queryOrderParam, SortedMap<Object, Object> parameters) {
        parameters.put("appid", queryOrderParam.getAppid());
        parameters.put("mch_id", queryOrderParam.getMchId());
        parameters.put("nonce_str", queryOrderParam.getNonceStr());

        if (null != queryOrderParam.getTransactionId()) {
            parameters.put("transaction_id", queryOrderParam.getTransactionId());
        }

        if (null != queryOrderParam.getOutTradeNo()) {
            parameters.put("out_trade_no", queryOrderParam.getOutTradeNo());
        }
    }

    private static void checkQueryOrderRequiredParams(QueryOrderParam queryOrderParam) throws WechatServiceException {
        if (queryOrderParam.getAppid() == null) {
            throw new WechatServiceException("APPID 不能为空。");
        }

        if (queryOrderParam.getMchId() == null) {
            throw new WechatServiceException("mch id 不能为空。");
        }

        if (queryOrderParam.getNonceStr() == null) {
            throw new WechatServiceException("nonce str 不能为空。");
        }

        if (queryOrderParam.getTransactionId() == null && queryOrderParam.getOutTradeNo() == null) {

            throw new WechatServiceException("transaction id 和 out trade no  不能全为空。");
        }


    }

    public static boolean checkQueryOrderSign(WechatPayResult wechatPayResult, String key) {
        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
        params.put("return_msg", wechatPayResult.getReturnMsg());
        params.put("appid", wechatPayResult.getAppid());
        params.put("mch_id", wechatPayResult.getMchId());
        params.put("nonce_str", wechatPayResult.getNonceStr());
        params.put("result_code", wechatPayResult.getResultCode());
        params.put("return_code", wechatPayResult.getReturnCode());
        params.put("out_trade_no", wechatPayResult.getOutTradeNo());
        params.put("trade_state", wechatPayResult.getTradeState());
        params.put("trade_state_desc", wechatPayResult.getTradeStateDesc());
        return checkSign(params,wechatPayResult.getSign(),key);
    }
}
