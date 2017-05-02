package com.joker.module.payment.alipay;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2017/4/27.
 */
public class AlipayConfig {
    // 商户appid
    public static String APPID ;
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY ;
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String NOTIFY_URL;
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String RETURN_URL;
    // 请求网关地址
    //public static String URL = "https://openapi.alipay.com/gateway.do";
    public static String URL;
    // 编码
    public static String CHARSET;
    // 返回格式
    public static String FORMAT;
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY;
    // 日志记录目录
    public static String log_path;
    // RSA2
    public static String SIGNTYPE;
    //超时时间 可空
    public static String TIMEOUT_EXPRESS;
    //product code

    // 销售产品码 必填
    public static String PRODUCT_CODE_QUICK_WAP_PAY = "QUICK_WAP_PAY";


    static {
        Properties properties = new Properties();
        try {
            properties.load(AlipayConfig.class.getClassLoader().getResourceAsStream("alipay-config.properties"));
            APPID = properties.getProperty("APPID");
            RSA_PRIVATE_KEY = properties.getProperty("RSA_PRIVATE_KEY");
            NOTIFY_URL = properties.getProperty("NOTIFY_URL");
            RETURN_URL = properties.getProperty("RETURN_URL");
            URL = properties.getProperty("URL");
            CHARSET = properties.getProperty("CHARSET");
            FORMAT = properties.getProperty("FORMAT");
            ALIPAY_PUBLIC_KEY = properties.getProperty("ALIPAY_PUBLIC_KEY");
            log_path = properties.getProperty("log_path");
            SIGNTYPE = properties.getProperty("SIGNTYPE");
            TIMEOUT_EXPRESS = properties.getProperty("TIMEOUT_EXPRESS");
        } catch (IOException e) {
            e.printStackTrace();
       /*     try {
                properties.load(WechatPaymentConfig.class.getClassLoader().getResourceAsStream("wechat_default.properties"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }*/

        }


    }
}
