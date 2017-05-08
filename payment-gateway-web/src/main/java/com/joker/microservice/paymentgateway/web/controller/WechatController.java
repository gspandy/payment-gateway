package com.joker.microservice.paymentgateway.web.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.joker.microservice.paymentgateway.cofig.GlobalConfigure;
import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import com.joker.module.common.encrypt.util.EncryptUtil;
import com.joker.module.common.request.HttpUtil;
import com.joker.module.payment.wechat.config.WechatPaymentConfig;
import com.joker.module.payment.wechat.domain.WechatPrePayOrder;
import com.joker.module.payment.wechat.domain.WechatUserAuth;
import com.joker.module.payment.wechat.exception.WechatServiceException;
import com.joker.module.payment.wechat.service.WechatPaymentService;
import com.joker.module.payment.wechat.service.impl.WechatPaymentServiceImpl;
import com.joker.module.payment.wechat.util.CommonUtil;
import com.joker.module.payment.wechat.util.WechatPaymentUtil;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
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

    private static Logger logger = Logger.getLogger(WechatController.class);

    private WechatPaymentService wechatPaymentService;

    public WechatController() {
        this.wechatPaymentService = new WechatPaymentServiceImpl();
    }

    @RequestMapping(value = "/auth")

    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String redirectUrl = HttpUtil.serviceBasePath(request) + WechatPaymentConfig.AUTH_CALLBACK_URI;

        try {
            redirectUrl = wechatPaymentService.generateGetCodeURL(redirectUrl);
        } catch (WechatServiceException e) {
            e.printStackTrace();
        }
        response.sendRedirect(redirectUrl);
    }

    @RequestMapping(value = "/auth/callback")
    public ModelAndView authCallback(@RequestParam String code, @RequestParam String state, HttpServletRequest request, HttpServletResponse response) throws IOException {

        WechatUserAuth wechatUserAuth = null;
        try {
            wechatUserAuth = wechatPaymentService.getWechatUserAuth(code);
        } catch (WechatServiceException e) {
            e.printStackTrace();
        }

        request.getSession().setAttribute(WechatPaymentConfig.WECHAT_USER_AUTH_SESSION_KEY, wechatUserAuth);
        HttpUtil.setCookie(response, WechatPaymentConfig.WECHAT_USER_AUTH_SESSION_KEY, EncryptUtil.BASE64(JSONUtils.toJSONString(wechatUserAuth)));


        PaymentOrder paymentOrder = (PaymentOrder) HttpUtil.getSesssionAttribute(request, GlobalConfigure.SESSION_PAYMENT_ORDER_KEY);

        WechatPrePayOrder prePayOrder = null;

        try {
            int amount = Integer.parseInt(paymentOrder.getAmount() * 100 + "");
            prePayOrder = wechatPaymentService.generateOpenPrePayOrder(amount, paymentOrder.getTitle(), paymentOrder.getOutTradeNo(), wechatUserAuth.getOpenid(), WechatPaymentConfig.RETURN_URI, CommonUtil.getIpAddr(request));
        } catch (WechatServiceException e) {
            e.printStackTrace();
        }


        if (prePayOrder == null) {
            throw new RuntimeException("generate wechat openid preorder error");
        }

        logger.debug("创建的prepay order :" + prePayOrder.toString());

        String h5PayJson = WechatPaymentUtil.generateH5PayJson(prePayOrder);

        logger.debug("h5 json 为 :" + h5PayJson);

        request.setAttribute("h5PayJson", h5PayJson);
        request.setAttribute("returnURL",HttpUtil.serviceBasePath(request) + WechatPaymentConfig.RETURN_URI);

        return new ModelAndView("wechat-open-h5");

    }


}
