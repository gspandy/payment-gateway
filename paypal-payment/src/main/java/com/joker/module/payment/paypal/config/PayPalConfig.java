package com.joker.module.payment.paypal.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhangjian on 2016/7/14.
 */
public class PayPalConfig {

    /**
     * staging 环境
     */
    public static String ENVIRONMENT ;
    public static String CLIENT_ID;
    public static String CLIENT_SECRET;


    public static String USD_CURRENCY = "USD";
    public static String SGD_CURRENCY = "SGD";

    /**
     * paypal 支付方式
     */
    public static String PAYPAL_PAYMENT_METHOD = "paypal";
    public static String CREDICARD_PAYMENT_METHOD = "credit_card";


    /**
     * 重定向链接
     */

    public String returnUri = "/topup/paypal/{guid}";

    public static final Map<String,String> stagingSettings = new HashMap<String, String>();

    static {
        Properties properties = new Properties();
        try {
            properties.load(PayPalConfig.class.getClassLoader().getResourceAsStream("paypal_config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ENVIRONMENT = (String) properties.get("environment");
        CLIENT_ID = (String) properties.get("clientId");
        CLIENT_SECRET = (String) properties.get("clientSecret");
    }

}
