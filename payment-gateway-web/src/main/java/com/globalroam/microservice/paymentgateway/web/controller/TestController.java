package com.globalroam.microservice.paymentgateway.web.controller;

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
    public String test(HttpServletRequest request) {
        System.out.println(request.getParameter("id"));
        return "success";
    }
}
