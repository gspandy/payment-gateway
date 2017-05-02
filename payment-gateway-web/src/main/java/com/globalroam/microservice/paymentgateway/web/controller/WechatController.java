package com.globalroam.microservice.paymentgateway.web.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.joker.module.common.encrypt.util.EncryptUtil;
import com.joker.module.common.request.HttpUtil;
import com.joker.module.payment.wechat.config.WechatPaymentConfig;
import com.joker.module.payment.wechat.domain.WechatUserAuth;
import com.joker.module.payment.wechat.exception.WechatServiceException;
import com.joker.module.payment.wechat.service.WechatPaymentService;
import com.joker.module.payment.wechat.service.impl.WechatPaymentServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Joker on 2017/5/2.
 */
@RestController
@ApiIgnore
@RequestMapping(value = "/public/v1/payment/wechat")
public class WechatController {

    private  WechatPaymentService wechatPaymentService;

    public WechatController() {
        this.wechatPaymentService = new WechatPaymentServiceImpl();
    }

    @RequestMapping(value = "/auth")

    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String redirectUrl = HttpUtil.serviceBasePath(request) + WechatPaymentConfig.AUTH_CALLBACK_URI;
        response.sendRedirect(redirectUrl);
    }

    @RequestMapping(value = "/auth/callback")
    public void authCallback(@RequestParam String code,@RequestParam String state,HttpServletRequest request, HttpServletResponse response) throws IOException {

        WechatUserAuth wechatUserAuth = null;
        try {
            wechatUserAuth = wechatPaymentService.getWechatUserAuth(code);
        } catch (WechatServiceException e) {
            e.printStackTrace();
        }

        request.getSession().setAttribute(WechatPaymentConfig.WECHAT_USER_AUTH_SESSION_KEY, wechatUserAuth);
        HttpUtil.setCookie(response,WechatPaymentConfig.WECHAT_USER_AUTH_SESSION_KEY,EncryptUtil.BASE64(JSONUtils.toJSONString(wechatUserAuth)));


    }


}
