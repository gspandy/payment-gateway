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
@RequestMapping(value = "/public/v1/test")
public class TestController {

    @RequestMapping("")
    public String test(@ModelAttribute("form") PaymentOrder paymentOrder,HttpServletRequest request) {
        System.out.println(paymentOrder.toString());
        System.out.println(request.getParameter("result"));
        System.out.println(request.getParameter("message"));
        return "success";
    }
}
