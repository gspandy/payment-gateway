package com.joker.module.payment.wechat.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.joker.module.common.httpclient.domain.Response;
import com.joker.module.payment.wechat.config.WechatPaymentConfig;
import com.joker.module.payment.wechat.domain.*;
import com.joker.module.payment.wechat.exception.LongURLException;
import com.joker.module.payment.wechat.exception.WechatOrderException;
import com.joker.module.payment.wechat.exception.WechatServiceException;
import com.joker.module.payment.wechat.http.WechatPaymentHttpClient;
import com.joker.module.payment.wechat.service.WechatPaymentService;
import com.joker.module.payment.wechat.util.CommonUtil;
import com.joker.module.payment.wechat.util.WechatPaymentUtil;
import com.joker.module.common.qrcode.util.QrcodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhangjian on 2016/8/9.
 */
public class WechatPaymentServiceImpl implements WechatPaymentService {

    private static Logger logger = LogManager.getLogger(WechatPaymentServiceImpl.class);

    @Override
    public String generateGetCodeURL(String returnUrl) throws WechatServiceException {

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("appid", WechatPaymentConfig.APPID);// 	公众号的唯一标识
        params.put("redirect_uri", returnUrl);// 	授权后重定向的回调链接地址，请使用urlencode对链接进行处理
        params.put("response_type", "code");// 	返回类型，请填写code
        params.put("scope", "snsapi_userinfo");//应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
        params.put("state", "wx090eef3f75d06436");// 	重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
        String url = WechatPaymentHttpClient.OPEN_ACCOUNT_LOGIN_REDIRECT_URL + "?" + WechatPaymentUtil.getParamsStr(params) + "#wechat_redirect";// 	无论直接打开还是做页面302重定向时候，必须带此参数
        logger.debug("用户登录授权重定向链接 = [" + url + "]");
        return url;
    }

    @Override
    public WechatUserAuth getWechatUserAuth(String code) throws WechatServiceException {

        if (CommonUtil.isEmpty(code)) {
            throw new WechatServiceException("微信重定向返回的code参数为空请检查网络是否正常。");
        }

        WechatPaymentHttpClient client = new WechatPaymentHttpClient(WechatPaymentHttpClient.ACCESS_TOKEN_ADDRESS);

        StringBuilder reqURI = new StringBuilder(WechatPaymentHttpClient.ACCESS_TOKEN_URI);
        reqURI.append("?");
        reqURI.append("appid=");
        reqURI.append(WechatPaymentConfig.APPID);
        reqURI.append("&secret=");
        reqURI.append(WechatPaymentConfig.SECRET);
        reqURI.append("&code=");
        reqURI.append(code);
        reqURI.append("&grant_type=authorization_code");

        Response response = client.get(reqURI.toString());

        if (!response.isSuccess()) {
            throw new WechatServiceException("调用微信接口异常，请检查网络是否正常。");
        }

        WechatUserAuth wechatUserAuth = null;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        try {
            wechatUserAuth = objectMapper.readValue(response.getString(), WechatUserAuth.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebServiceException("转换微信认证openid相关数据失败，请检查数据格式");
        }

        return wechatUserAuth;
    }

    @Override
    public WechatPrePayOrder generateOpenPrePayOrder(int mount, String tittle, String outTradeNo, String openid, String notifyUrl, String ip) throws WechatServiceException {

        WechatOrder wechatOrder = new WechatOrder();
        wechatOrder.setKey(WechatPaymentConfig.KEY);
        wechatOrder.setAppid(WechatPaymentConfig.APPID);
        wechatOrder.setDeviceInfo(WechatOrder.DEVICE_INFO_WEB);
        wechatOrder.setMchId(WechatPaymentConfig.MCH_ID);
        wechatOrder.setBody(tittle);
        wechatOrder.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
        wechatOrder.setNotifyUrl(notifyUrl);
        wechatOrder.setOpenid(openid);
        wechatOrder.setOutTradeNo(outTradeNo);
        wechatOrder.setSpbillCreateIp(ip);
        wechatOrder.setTotalFee(mount);
        wechatOrder.setTradeType(WechatOrder.TRADE_TYPE_JSAPI);


        logger.debug("wechat 下单参数:" + wechatOrder.toString());

        String xmlParams = null;
        xmlParams = getOrderParamsXMLString(wechatOrder, xmlParams);

        WechatPaymentHttpClient client = new WechatPaymentHttpClient(WechatPaymentHttpClient.ORDER_ADDRESS);
        Response response = client.post(WechatPaymentHttpClient.CREATE_ORDER_URI, xmlParams);

        if (!response.isSuccess()) {
            logger.debug("WechatPaymentServiceImpl.generateOpenPrePayOrder :" + response.getString());
            throw new WechatServiceException("创建生成预支付订单失败");
        }

        WechatPrePayOrder wechatPrePayOrder = null;
        wechatPrePayOrder = getWechatPrePayOrder(response.getString());

        if (wechatPrePayOrder != null && WechatPrePayOrder.RETURN_CODE_FAIL.equalsIgnoreCase(wechatPrePayOrder.getReturnCode())) {
            throw new WebServiceException("微信接口调用失败:原因" + wechatPrePayOrder.getReturnMsg());
        }

        return wechatPrePayOrder;
    }

    @Override
    public WechatPrePayOrder generateOpenPrePayOrder(int mount, String tittle, String outTradeNo,String attach, String openid, String notifyUrl, String ip) throws WechatServiceException {

        WechatOrder wechatOrder = new WechatOrder();
        wechatOrder.setKey(WechatPaymentConfig.KEY);
        wechatOrder.setAppid(WechatPaymentConfig.APPID);
        wechatOrder.setDeviceInfo(WechatOrder.DEVICE_INFO_WEB);
        wechatOrder.setMchId(WechatPaymentConfig.MCH_ID);
        wechatOrder.setBody(tittle);
        wechatOrder.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
        wechatOrder.setNotifyUrl(notifyUrl);
        wechatOrder.setOpenid(openid);
        wechatOrder.setOutTradeNo(outTradeNo);
        wechatOrder.setSpbillCreateIp(ip);
        wechatOrder.setTotalFee(mount);
        wechatOrder.setTradeType(WechatOrder.TRADE_TYPE_JSAPI);
        wechatOrder.setAttach(attach);


        logger.debug("wechat 下单参数:" + wechatOrder.toString());

        String xmlParams = null;
        xmlParams = getOrderParamsXMLString(wechatOrder, xmlParams);

        WechatPaymentHttpClient client = new WechatPaymentHttpClient(WechatPaymentHttpClient.ORDER_ADDRESS);
        Response response = client.post(WechatPaymentHttpClient.CREATE_ORDER_URI, xmlParams);

        if (!response.isSuccess()) {
            logger.debug("WechatPaymentServiceImpl.generateOpenPrePayOrder :" + response.getString());
            throw new WechatServiceException("创建生成预支付订单失败");
        }

        WechatPrePayOrder wechatPrePayOrder = null;
        wechatPrePayOrder = getWechatPrePayOrder(response.getString());

        if (wechatPrePayOrder != null && WechatPrePayOrder.RETURN_CODE_FAIL.equalsIgnoreCase(wechatPrePayOrder.getReturnCode())) {
            throw new WebServiceException("微信接口调用失败:原因" + wechatPrePayOrder.getReturnMsg());
        }

        return wechatPrePayOrder;
    }


    private WechatPrePayOrder getWechatPrePayOrder(String data) {
        WechatPrePayOrder wechatPrePayOrder;
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            wechatPrePayOrder = xmlMapper.readValue(data, WechatPrePayOrder.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WebServiceException("微信返回数据转换失败，数据格式问题,可能是网络不通，或者参数有问题，请仔细检查。");
        }

        return wechatPrePayOrder;
    }

    @Override
    public WechatPrePayOrder generateURLPrePayOrder(int mount, String tittle, String productId, String outTradeNo, String notifyUrl, String ip) throws WechatServiceException {
        WechatOrder wechatOrder = new WechatOrder();
        wechatOrder.setKey(WechatPaymentConfig.KEY);
        wechatOrder.setAppid(WechatPaymentConfig.APPID);
        wechatOrder.setDeviceInfo(WechatOrder.DEVICE_INFO_WEB);
        wechatOrder.setMchId(WechatPaymentConfig.MCH_ID);
        wechatOrder.setTradeType(WechatOrder.TRADE_TYPE_NATIVE);
        wechatOrder.setBody(tittle);
        wechatOrder.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
        wechatOrder.setNotifyUrl(notifyUrl);
        wechatOrder.setOutTradeNo(outTradeNo);
        wechatOrder.setSpbillCreateIp(ip);
        wechatOrder.setProductId(productId);
        wechatOrder.setTotalFee(mount);


        logger.debug("wechat 下单参数:" + wechatOrder.toString());

        String xmlParams = null;
        xmlParams = getOrderParamsXMLString(wechatOrder, xmlParams);

        WechatPaymentHttpClient client = new WechatPaymentHttpClient(WechatPaymentHttpClient.ORDER_ADDRESS);
        Response response = client.post(WechatPaymentHttpClient.CREATE_ORDER_URI, xmlParams);

        if (!response.isSuccess()) {
            logger.debug("WechatPaymentServiceImpl.generateOpenPrePayOrder :" + response.getString());
            throw new WechatServiceException("创建生成预支付订单失败");
        }

        WechatPrePayOrder wechatPrePayOrder = null;
        wechatPrePayOrder = getWechatPrePayOrder(response.getString());

        if (wechatPrePayOrder != null && WechatPrePayOrder.RETURN_CODE_FAIL.equalsIgnoreCase(wechatPrePayOrder.getReturnCode())) {
            throw new WebServiceException("微信接口调用失败:原因" + wechatPrePayOrder.getReturnMsg());
        }

        return wechatPrePayOrder;
    }

    @Override
    public WechatPrePayOrder generateURLPrePayOrder(int mount, String tittle, String productId, String outTradeNo, String attach, String notifyUrl, String ip) throws WechatServiceException {
        WechatOrder wechatOrder = new WechatOrder();
        wechatOrder.setKey(WechatPaymentConfig.KEY);
        wechatOrder.setAppid(WechatPaymentConfig.APPID);
        wechatOrder.setDeviceInfo(WechatOrder.DEVICE_INFO_WEB);
        wechatOrder.setMchId(WechatPaymentConfig.MCH_ID);
        wechatOrder.setTradeType(WechatOrder.TRADE_TYPE_NATIVE);
        wechatOrder.setBody(tittle);
        wechatOrder.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
        wechatOrder.setNotifyUrl(notifyUrl);
        wechatOrder.setOutTradeNo(outTradeNo);
        wechatOrder.setSpbillCreateIp(ip);
        wechatOrder.setProductId(productId);
        wechatOrder.setTotalFee(mount);
        wechatOrder.setAttach(attach);


        logger.debug("wechat 下单参数:" + wechatOrder.toString());

        String xmlParams = null;
        xmlParams = getOrderParamsXMLString(wechatOrder, xmlParams);

        WechatPaymentHttpClient client = new WechatPaymentHttpClient(WechatPaymentHttpClient.ORDER_ADDRESS);
        Response response = client.post(WechatPaymentHttpClient.CREATE_ORDER_URI, xmlParams);

        if (!response.isSuccess()) {
            logger.debug("WechatPaymentServiceImpl.generateOpenPrePayOrder :" + response.getString());
            throw new WechatServiceException("创建生成预支付订单失败");
        }

        WechatPrePayOrder wechatPrePayOrder = null;
        wechatPrePayOrder = getWechatPrePayOrder(response.getString());

        if (wechatPrePayOrder != null && WechatPrePayOrder.RETURN_CODE_FAIL.equalsIgnoreCase(wechatPrePayOrder.getReturnCode())) {
            throw new WebServiceException("微信接口调用失败:原因" + wechatPrePayOrder.getReturnMsg());
        }

        return wechatPrePayOrder;
    }

    @Override
    public WechatPrePayOrder generateMWEBPrePayOrder(int mount, String tittle, String productId, String outTradeNo, String attach, String notifyUrl, String ip) throws WechatServiceException {
        WechatOrder wechatOrder = new WechatOrder();
        wechatOrder.setKey(WechatPaymentConfig.KEY);
        wechatOrder.setAppid(WechatPaymentConfig.APPID);
        wechatOrder.setDeviceInfo(WechatOrder.DEVICE_INFO_WEB);
        wechatOrder.setMchId(WechatPaymentConfig.MCH_ID);
        wechatOrder.setTradeType(WechatOrder.TRADE_TYPE_MWEB);
        wechatOrder.setBody(tittle);
        wechatOrder.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
        wechatOrder.setNotifyUrl(notifyUrl);
        wechatOrder.setOutTradeNo(outTradeNo);
        wechatOrder.setSpbillCreateIp(ip);
        wechatOrder.setProductId(productId);
        wechatOrder.setTotalFee(mount);
        wechatOrder.setAttach(attach);


        logger.debug("wechat 下单参数:" + wechatOrder.toString());

        String xmlParams = null;
        xmlParams = getOrderParamsXMLString(wechatOrder, xmlParams);

        WechatPaymentHttpClient client = new WechatPaymentHttpClient(WechatPaymentHttpClient.ORDER_ADDRESS);
        Response response = client.post(WechatPaymentHttpClient.CREATE_ORDER_URI, xmlParams);

        if (!response.isSuccess()) {
            logger.debug("WechatPaymentServiceImpl.generateOpenPrePayOrder :" + response.getString());
            throw new WechatServiceException("创建生成预支付订单失败");
        }

        WechatPrePayOrder wechatPrePayOrder = null;
        wechatPrePayOrder = getWechatPrePayOrder(response.getString());

        if (wechatPrePayOrder != null && WechatPrePayOrder.RETURN_CODE_FAIL.equalsIgnoreCase(wechatPrePayOrder.getReturnCode())) {
            throw new WebServiceException("微信接口调用失败:原因" + wechatPrePayOrder.getReturnMsg());
        }

        return wechatPrePayOrder;
    }


    @Override
    public WechatPayResult parseNotifyXMLData(String xmlData) throws WechatServiceException {

        WechatPayResult wechatPayResult = WechatPaymentUtil.parseNotifyXmlData(xmlData);

        if(null == wechatPayResult){
            throw new WechatServiceException("数据为空，请求异常，或者数据格式不对。");
        }

        boolean result = WechatPaymentUtil.checkNotifyUrlSign(wechatPayResult,WechatPaymentConfig.KEY);


        if(!WechatPaymentConfig.APPID.equals(wechatPayResult.getAppid())){
            throw new WechatServiceException("APPID 对应不上，可能是伪装数据");
        }

        if(result){
            return  wechatPayResult;
        }
         throw new WechatServiceException("签名有问题，可能是非法通知。");
    }


    @Override
    public WechatPayResult parseQueryOrderXMLData(String xmlData) throws WechatServiceException {

        WechatPayResult wechatPayResult = WechatPaymentUtil.parseNotifyXmlData(xmlData);

        if(null == wechatPayResult){
            throw new WechatServiceException("数据为空，请求异常，或者数据格式不对。");
        }

        boolean result = WechatPaymentUtil.checkQueryOrderSign(wechatPayResult,WechatPaymentConfig.KEY);


        if(!WechatPaymentConfig.APPID.equals(wechatPayResult.getAppid())){
            throw new WechatServiceException("APPID 对应不上，可能是伪装数据");
        }

        if(result){
            return  wechatPayResult;
        }
        throw new WechatServiceException("签名有问题，可能是非法通知。");
    }

    @Override
    public String transferLong2ShortURL(String longURL) throws WechatServiceException {

        if(longURL == null || "".equals(longURL)){
            throw  new WechatServiceException("长连接为空，请仔细检查参数");
        }

        LongURL longURL1 = new LongURL();
        longURL1.setAppid(WechatPaymentConfig.APPID);
        longURL1.setMchId(WechatPaymentConfig.MCH_ID);
        longURL1.setNonceStr(UUID.randomUUID().toString().replaceAll("-",""));
        longURL1.setLongUrl(longURL);

        Response response = null;
        try {
            String xmlParams = WechatPaymentUtil.generateSortedXMLFromLongURL(longURL1);
            WechatPaymentHttpClient client = new WechatPaymentHttpClient(WechatPaymentHttpClient.ORDER_ADDRESS);
            response = client.post(WechatPaymentHttpClient.LONG_TO_SHORT_URI, xmlParams);

        } catch (LongURLException e) {
            e.printStackTrace();
            throw new WechatServiceException("转换参数为xml失败");
        }

        if(!response.isSuccess()){
            throw new WechatServiceException("访问微信接口转换链接失败，检查网络或者参数是否正确");
        }

        XmlMapper mapper = new XmlMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        ShortURL shortURL = null;
        try {
            shortURL = mapper.readValue(response.getString(),ShortURL.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WechatServiceException("解析微信返回结果数据失败，检查字段映射是否正确");
        }

        if(shortURL == null || !shortURL.isSuccess()){
            throw new WechatServiceException("解析微信返回结果数据失败，检查调用微信接口参数。");
        }



        return shortURL.getShortUrl();
    }

    @Override
    public void generateQRCodePrePayOrder(int mount, String tittle, String productId, String outTradeNo, String notifyUrl, String ip, OutputStream outputStream) throws WechatServiceException {
        WechatPrePayOrder wechatPrePayOrder = generateURLPrePayOrder(mount,tittle,productId,outTradeNo,notifyUrl,ip);
       // String shortURL = transferLong2ShortURL(wechatPrePayOrder.getCodeUrl());

        try {
            QrcodeUtils.gen(wechatPrePayOrder.getCodeUrl(),outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WechatServiceException("生成二维码失败");
        }
    }

    @Override
    public void generateQRCodePrePayOrder(int mount, String tittle, String productId, String outTradeNo, String notifyUrl, String ip, OutputStream qrCodeOutPut, InputStream logoInput) throws WechatServiceException {
        WechatPrePayOrder wechatPrePayOrder = generateURLPrePayOrder(mount,tittle,productId,outTradeNo,notifyUrl,ip);
        // String shortURL = transferLong2ShortURL(wechatPrePayOrder.getCodeUrl());

        try {
            QrcodeUtils.gen(wechatPrePayOrder.getCodeUrl(),qrCodeOutPut,logoInput);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WechatServiceException("生成二维码失败");
        }
    }

    @Override
    public void generateQRCodePrePayOrder(int mount, String tittle, String productId, String outTradeNo, String notifyUrl, String ip, OutputStream outputStream, int width, int height) throws WechatServiceException {
        WechatPrePayOrder wechatPrePayOrder = generateURLPrePayOrder(mount,tittle,productId,outTradeNo,notifyUrl,ip);
       // String shortURL = transferLong2ShortURL(wechatPrePayOrder.getCodeUrl());

        try {
            QrcodeUtils.gen(wechatPrePayOrder.getCodeUrl(),outputStream,width,height);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WechatServiceException("生成二维码失败");
        }
    }

    @Override
    public void generateQRCodePrePayOrder(int mount, String tittle, String productId, String outTradeNo, String notifyUrl, String ip, OutputStream qrCodeOutPut, InputStream logoInput, int width, int height) throws WechatServiceException {
        WechatPrePayOrder wechatPrePayOrder = generateURLPrePayOrder(mount,tittle,productId,outTradeNo,notifyUrl,ip);
        // String shortURL = transferLong2ShortURL(wechatPrePayOrder.getCodeUrl());

        try {
            QrcodeUtils.gen(wechatPrePayOrder.getCodeUrl(),qrCodeOutPut,logoInput,width,height);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WechatServiceException("生成二维码失败");
        }
    }

    private String getOrderParamsXMLString(WechatOrder wechatOrder, String xmlParams) throws WechatServiceException {
        try {
            xmlParams = WechatPaymentUtil.generateSortedXMLFromWechatOrder(wechatOrder, WechatPaymentUtil.CHARACTER_ENCODING_UTF8);
        } catch (WechatOrderException e) {
            logger.debug("WechatPaymentServiceImpl.generateOpenPrePayOrder:" + xmlParams);
            e.printStackTrace();
            throw new WechatServiceException("创建生成请求数据失败");
        }
        return xmlParams;
    }


    @Override
    public WechatPayResult queryPayResult(String outTradeNo) throws WechatServiceException {

        if (null == outTradeNo || "".equals(outTradeNo)) {
            throw new WechatServiceException("outTradeNo 不能为空");
        }

        QueryOrderParam queryOrderParam = new QueryOrderParam();
        queryOrderParam.setKey(WechatPaymentConfig.KEY);
        queryOrderParam.setAppid(WechatPaymentConfig.APPID);
        queryOrderParam.setMchId(WechatPaymentConfig.MCH_ID);
        queryOrderParam.setOutTradeNo(outTradeNo);
        queryOrderParam.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));

        logger.debug("wechat 查询订单:" + queryOrderParam.toString());

        String xmlParams = null;
        xmlParams = getQueryOrderParamsXMLString(queryOrderParam);

        WechatPaymentHttpClient client = new WechatPaymentHttpClient(WechatPaymentHttpClient.ORDER_ADDRESS);
        Response response = client.post(WechatPaymentHttpClient.QUERY_ORDER_URI, xmlParams);

        if (!response.isSuccess()) {
            logger.debug("WechatPaymentServiceImpl.generateOpenPrePayOrder :" + response.getString());
            throw new WechatServiceException("查询订单失败");
        }

        WechatPayResult wechatPayResult = null;
        wechatPayResult = parseQueryOrderXMLData(response.getString());

        if (wechatPayResult != null && WechatPrePayOrder.RETURN_CODE_FAIL.equalsIgnoreCase(wechatPayResult.getReturnCode())) {
            throw new WebServiceException("微信接口调用失败:原因" + wechatPayResult.getReturnMsg());
        }

        return wechatPayResult;
    }

    private String getQueryOrderParamsXMLString(QueryOrderParam queryOrderParam) throws WechatServiceException {

        String xmlParams = WechatPaymentUtil.generateSortedXMLFromQueryOrderParams(queryOrderParam, WechatPaymentUtil.CHARACTER_ENCODING_UTF8);
        logger.debug("WechatPaymentServiceImpl.generateQueryOrder:" + xmlParams);
        return xmlParams;

    }
}
