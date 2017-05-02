package com.joker.module.common.request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Joker on 2017/5/2.
 */
public class HttpUtil {

    private static Logger logger = LogManager.getLogger(HttpUtil.class);
    /**
     * 获取Cookie
     * @param request
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request,String name){
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            logger.debug("Request cookie size:" + cookies.length);
            for (Cookie cookie:cookies) {
                logger.debug("cookie name :" + cookie.getName());
                if(cookie.getName().equalsIgnoreCase(name)){
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void setCookie(HttpServletResponse response,String key, String value) {
        Cookie cookie = new Cookie(key,value);
        response.addCookie(cookie);
    }


    public static String serviceBasePath(HttpServletRequest request) {

        StringBuilder webPathBuilder = new StringBuilder();
        webPathBuilder.append(request.getScheme());
        webPathBuilder.append("://");
        webPathBuilder.append(request.getRemoteHost());
        webPathBuilder.append(":");
        webPathBuilder.append(request.getRemotePort());
        webPathBuilder.append(request.getContextPath());

        String webPath = webPathBuilder.toString();
        logger.debug("web path from request : " + webPath);
        return webPath;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        //ipAddress = this.getRequest().getRemoteAddr();
        ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }

        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }

        logger.debug("web ip address from request : " + ipAddress);

        return ipAddress;
    }

}
