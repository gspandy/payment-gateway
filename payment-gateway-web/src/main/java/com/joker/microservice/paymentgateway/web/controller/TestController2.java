package com.joker.microservice.paymentgateway.web.controller;

import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/5/5.
 */
@RestController
@ApiIgnore
@RequestMapping(value = "/paygw/public/v1/payment/notification/alipay")
public class TestController2 {

    @RequestMapping("")
    public String test() {
        return "success";
    }
}
