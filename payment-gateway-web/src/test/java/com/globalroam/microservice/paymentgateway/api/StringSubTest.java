package com.globalroam.microservice.paymentgateway.api;

import org.junit.Test;

/**
 * Created by Joker on 2017/5/5.
 */
public class StringSubTest {

    @Test
    public void test() {
       // String url1 = "http://joker.iask.in/aaa/bbb/ccc";
        String url1 = "https://joker.iask.in/aaa/bbb/ccc";

        String prototype = url1.substring(0, url1.indexOf("://"));
        System.out.println("prototype : " + prototype);

        String restURL = url1.substring(url1.indexOf("://") + 3, url1.length());
        int port = 80;

        if ("http".equalsIgnoreCase(prototype)) {
            port = 80;
        } else if ("https".equalsIgnoreCase(prototype)) {
            port = 443;
        }
        String address = "";
        String uri = "";
        if (restURL.indexOf(":") >= 0) {
            address = restURL.substring(0, restURL.indexOf(":"));
            port = Integer.parseInt(restURL.substring(restURL.indexOf(":") + 1, restURL.indexOf("/")));
            uri = restURL.substring(restURL.indexOf("/"), restURL.length());
        } else {
            address = restURL.substring(0, restURL.indexOf("/"));
            uri = restURL.substring(restURL.indexOf("/"), restURL.length());
        }
        System.out.println("address : " + address);
        System.out.println("port : " + port);
        System.out.println("uri : " + uri);

    }
}
