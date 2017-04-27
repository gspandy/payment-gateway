package com.joker.module.payment.alipay;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2017/4/27.
 */
public class AlipayConfig {
    // 商户appid
    public static String APPID = "2016080200148659";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCgCKlLb5ifXcssXg8zuklYeyxvvqnJLCJwZjywtxQC4B7lmbYq1zjqdKAeCBq5JnqczWOyLBXUhT8jY9tY0IgV3GSH9sXqfgGnrV+2TJ62ZeyU16sjj9/YaJDNvC9nbSdCIuw3hHNZOp/Z/Fzl9M2QklFxoClVPgZyAamoByb31/wncT2+dc5HLqlW2BJfVwpquBSCiAp8mN/mHpEDTD2MZjwXb+UD8Ay+yae/HB24rj/RUXuaszCuMmZfBxS7agbhV+ljXjeyL+MiZ0WwoB9ByydPV6vFBnMIfOfRmBTkaIgBO+d1KMQugDguyw6SsEZBT+1Db7rEygvinbVxN1+LAgMBAAECggEALv58IF/T9LCJUi0Iz8rbcPUytlnJ5/5i2pjbJ5WLddC0of3CZdAvsYGv50G//zDSfqs9pHuXqYG7abwQFIOgArNmfhq0Qcjg2Ylq+tCDJtLFZ2vxoI7GT/lc1F13miqOhbhVn3Pwn3o8qLmX2htKJjMhYXmDybleESp11JbNFahKqhnza/IUmfiJS++/ys7lxa2RCx8oNqZEnwcrHzaDjBN5+QKTDEznRsJNJkzmk2R1s3qPVRjdXInWSel85iPWH9GCg+tPV31L7YbGTEzwD3NHdJ0oovQnP9f43/WFr6BoQ8DagrXfVVRN6cRgfUL1K0Cx1usoda2sqekJExbl6QKBgQDgjroSUurDh0WYbuOTLC4aL8WgE+JauL43qwNe7m6qpdA365Zwe3I4bH4TMkWm46Q3Bs71Lz90L8AcG4vdCwImjoHLQzde469AxSRCNP7JxC+yo8xh+hW7hwfs2vJ5JofGurmGmFzs/H0/Mcyy6Ve8IJ4Aamsf0edPXXJ0KyfLjQKBgQC2cRSHzhDGS8tHaXpA8DbbqFAbDyX7Pnr1NN7NRc8asdDnf6i3IBwyeobNfRnC7g9hbJRyEm5zAei1S8xSw5B79qF2mxe0oKDOfWrDRb1zLQgVGHQ4DzkavWS9/L9QFyJZb8jIffkv5DZNaoUJ+k9lL1DfFcuqwQA1nYCrFzYFdwKBgBJxxrjQlLXgu4esvAaPn23JNLtURsha2kzqQAmhHffoTLT7Jc0xZo1Z4/UhsPTnR8q5RXCAI/8gGPB2FsPeLpwKIoEWkUq6+t8gEj1Z6v+ZnK1YpOX7S6OukKkUxsvKrWZWWfzDMQbKKmNxbLXZUrqCF1hDZW8kp5IuQ8Nk9AzVAoGAGqnIIFjjZrepC9TEgz0YJ14f5vR9zoxBGBkT/tgW/oXCTDMCC1FsDYyOqvV/kFeOc+SUhV9gOPqsceKUB/yA3z0aN+VOv51CIyt9MPC+UVjhd4PNaGwBIoFPedy4cRIQeRW3OGYAxxkJPqM0slKcy+LwZMogq+HtD+jm5xpLUN0CgYBM0TnRWyhEf5MPyATqUsOV+v01BtaqLITlQUsXZ80eYtvrUf/zzkm91B6sQubCdkdVfIp+qgvdBg2CrysdQHLYXC80aO49Tom1J2UnWhOC9hn7SMx1/7g+qCZDN9Mo93MlEoeBom4J2fCydgBp51bz0hygloA3FwSDJ5iUmEQZiA==";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://joker.iask.in/notify_url.jsp";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://joker.iask.in/return_url.jsp";
    // 请求网关地址
    //public static String URL = "https://openapi.alipay.com/gateway.do";
    public static String URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAutD8IVIJRw8ff83NOqsmsCqHK3vZssRk8/VSM+ZQ8x5uk8I9cDgcWnSOjYnDuIZaD9HrsWqAsPgzShjW6qt+LbrR7y1aTcEhMDGpWtpQOwmcayoYeZrZL13Ma/zI033gPXXH58J5tqVTF9TZOObh5s3JPCiE8cZ1mve7I1hzFqA4B4C2FptCtSp/xWD1k8QDexaBdEvyaYiQAh4OjKByvwzdX3gFkK2fBYeLxKIvrRpcNuSw15yPXSVa1w3ojaBzeB1FUybpYhZOLKje94EoA7SFZX5SDwEdmAR1MJ0Tz4i+Xkdo4aCaNUx1ZgTkoj8hXxghb8obZJU5IhxjqkPjbwIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";


    static {
        Properties properties = new Properties();
        try {
            properties.load(AlipayConfig.class.getClassLoader().getResourceAsStream("alipay_default.properties"));
            APPID = properties.getProperty("APPID");
            RSA_PRIVATE_KEY = properties.getProperty("RSA_PRIVATE_KEY");
            notify_url = properties.getProperty("notify_url");
            return_url = properties.getProperty("return_url");
            URL = properties.getProperty("URL");
            CHARSET = properties.getProperty("CHARSET");
            FORMAT = properties.getProperty("FORMAT");
            ALIPAY_PUBLIC_KEY = properties.getProperty("ALIPAY_PUBLIC_KEY");
            log_path = properties.getProperty("log_path");
            SIGNTYPE = properties.getProperty("SIGNTYPE");
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
