package com.joker.module.payment.wechat.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhangjian on 2016/8/9.
 */
public class WechatPaymentConfig {
    public static String KEY;
    public static String APPID;
    public static String MCH_ID;
    public static String SECRET;
    public static String WECHAT_USER_AUTH_SESSION_KEY = "wechatUserAuth";
    public static String AUTH_CALLBACK_URI;

    public static String WECHAT_QR_LOGO_NAME = "logo.jpg";
    public static String AUTH_URI;
    public static String RETURN_URI;

    static {
        Properties properties = new Properties();
        try {
            properties.load(WechatPaymentConfig.class.getClassLoader().getResourceAsStream("wechat_default.properties"));

            KEY = properties.getProperty("key");
            APPID = properties.getProperty("appid");
            MCH_ID = properties.getProperty("mch_id");
            SECRET = properties.getProperty("secret");
            AUTH_CALLBACK_URI = properties.getProperty("auth_callback_uri");
            AUTH_URI = properties.getProperty("auth_uri");
            RETURN_URI = properties.getProperty("return_uri");
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
