package com.joker.module.common.request;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
        String clientIp = request.getHeader("X-Real-IP");
        webPathBuilder.append(request.getServerName());

        if(80 != request.getServerPort()){
            webPathBuilder.append(":");
            webPathBuilder.append(request.getServerPort());
        }

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

    public static void setSessionAttribute(HttpServletRequest request, String sessionPaymentOrderKey, Object object) {
        request.getSession().setAttribute(sessionPaymentOrderKey, object);
    }

    public static Object getSesssionAttribute(HttpServletRequest request, String sessionPaymentOrderKey) {
        return request.getSession().getAttribute(sessionPaymentOrderKey);
    }

    public static Host parseURL(String url) {

        logger.debug("parse URL : " + url);

        String prototype = url.substring(0, url.indexOf("://"));
        System.out.println("prototype : " + prototype);

        String restURL = url.substring(url.indexOf("://") + 3, url.length());
        String port = "80";

        if ("http".equalsIgnoreCase(prototype)) {
            port = "80";
        } else if ("https".equalsIgnoreCase(prototype)) {
            port = "443";
        }


        String address = "";
        String uri = "";
        if (restURL.indexOf(":") >= 0) {
            address = restURL.substring(0, restURL.indexOf(":"));
            port = restURL.substring(restURL.indexOf(":") + 1, restURL.indexOf("/"));
            uri = restURL.substring(restURL.indexOf("/"), restURL.length());
        } else {
            address = restURL.substring(0, restURL.indexOf("/"));
            uri = restURL.substring(restURL.indexOf("/"), restURL.length());
        }
        System.out.println("address : " + address);
        System.out.println("port : " + port);
        System.out.println("uri : " + uri);
        Host host = new Host(prototype, address, port, uri);
        return host;
    }

    public static class Host {
        private String httpType;
        private String address;
        private String port;
        private String uri;

        public Host() {
        }

        public Host(String httpType, String address, String port, String uri) {
            this.httpType = httpType;
            this.address = address;
            this.port = port;
            this.uri = uri;
        }

        public String getHttpType() {
            return httpType;
        }

        public void setHttpType(String httpType) {
            this.httpType = httpType;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

}
