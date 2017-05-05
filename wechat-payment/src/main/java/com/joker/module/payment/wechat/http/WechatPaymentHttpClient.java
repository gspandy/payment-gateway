package com.joker.module.payment.wechat.http;

import com.joker.module.common.httpclient.HttpRequest;
import org.apache.commons.httpclient.Header;

/**
 * Created by zhangjian on 2016/7/31.
 */
public class WechatPaymentHttpClient extends HttpRequest {

    private static String HTTP_TYPE = "https";
    private static int HTTP_443 = 443;
    public static String ORDER_ADDRESS = "api.mch.weixin.qq.com";
    public static String CREATE_ORDER_URI = "/pay/unifiedorder";
    public static String REFUND_ORDER_URI = "/secapi/pay/refund";
    public static String LONG_TO_SHORT_URI = "/tools/shorturl";
    public static String QUERY_ORDER_URI = "/pay/orderquery";

    public static String ACCESS_TOKEN_ADDRESS = "api.weixin.qq.com";
    public static String ACCESS_TOKEN_URI = "/sns/oauth2/access_token";

    public static String OPEN_ACCOUNT_LOGIN_REDIRECT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    private static Header[] XML_REQUEST_HEADERS = {new Header(CONTENT_TYPE_KEY,CONTENT_TYPE_XML),new Header(ACCEPT_KEY,ACCEPT_XML)};
    public WechatPaymentHttpClient(String address) {
        super(HTTP_TYPE, XML_REQUEST_HEADERS, address, HTTP_443);
    }
}
