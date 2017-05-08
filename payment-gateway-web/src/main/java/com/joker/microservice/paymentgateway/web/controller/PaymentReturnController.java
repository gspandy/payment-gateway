package com.joker.microservice.paymentgateway.web.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import com.joker.microservice.paymentgateway.exception.DataNotFoundException;
import com.joker.microservice.paymentgateway.exception.ServiceException;
import com.joker.microservice.paymentgateway.service.PaymentOrderService;
import com.joker.module.payment.alipay.AlipayConfig;
import com.joker.module.payment.wechat.domain.WechatPayResult;
import com.joker.module.payment.wechat.exception.WechatServiceException;
import com.joker.module.payment.wechat.service.WechatPaymentService;
import com.joker.module.payment.wechat.service.impl.WechatPaymentServiceImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Joker on 2017/4/30.
 */
@RestController
@ApiIgnore
@RequestMapping(value = "/public/v1/payment/return")
public class PaymentReturnController {

    public static final String SUCCESS_VIEW = "success";
    public static final String ERROR_VIEW = "error";

    private Logger logger = LogManager.getLogger(PaymentReturnController.class);

    @Autowired
    private PaymentOrderService paymentOrderService;

    @RequestMapping(value = "/alipay", method = RequestMethod.GET)
    public ModelAndView alipayReturnNotification(HttpServletRequest request,HttpServletResponse response) {

        logger.info("================= aplipay return notify start =================");
        ModelAndView modelAndView = new ModelAndView();
        PaymentOrder paymentOrder = null;
        //获取支付宝GET过来反馈信息
        try {
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }

            //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            //商户订单号

            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            logger.info("outTradeNo is : " + outTradeNo);

            //支付宝交易号

            String tradeNo = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            logger.info("tradeNo is : " + tradeNo);

            double amount = Double.parseDouble(request.getParameter("total_amount"));

            //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            //计算得出通知验证结果
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);

            paymentOrder = paymentOrderService.getByAmountAndOutTradeNo(outTradeNo,amount);

            if (paymentOrder == null) {
                paymentOrder = new PaymentOrder();
                modelAndView.addObject("paymentOrder", paymentOrder);
                modelAndView.addObject("result", "FAILED");
                modelAndView.addObject("message", "支付单流水号不存在。");
                modelAndView.setViewName(ERROR_VIEW);
            }

            paymentOrder.setTradeNo(tradeNo);
            if (verify_result) {//验证成功
                logger.info("sign verid is success.");
                // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
                AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
                AlipayTradeQueryRequest alipay_request = new AlipayTradeQueryRequest();

                AlipayTradeQueryModel model=new AlipayTradeQueryModel();
                model.setOutTradeNo(outTradeNo);
                model.setTradeNo(tradeNo);
                alipay_request.setBizModel(model);

                AlipayTradeQueryResponse alipayResponse =client.execute(alipay_request);

                logger.debug("alipay order request response body :" + alipayResponse.getBody());


                if (!AlipayTradeQueryResponse.TRADE_SUCCESS.equalsIgnoreCase(alipayResponse.getTradeStatus())) {
                    logger.info("payment order status is :  " + alipayResponse.getTradeStatus());
                    paymentOrder.setStatus(PaymentOrder.STATUS_FAIL);
                    paymentOrderService.update(paymentOrder);
                    modelAndView.addObject("paymentOrder", paymentOrder);
                    modelAndView.addObject("result", "FAILED");
                    modelAndView.addObject("message", "订单不是真的成功");
                    modelAndView.setViewName(ERROR_VIEW);
                    return modelAndView;
                }

      /*          if (paymentOrder == null || PaymentOrder.STATUS_SUCCESS.equalsIgnoreCase(paymentOrder.getStatus()) || PaymentOrder.STATUS_COMPLETE.equalsIgnoreCase(paymentOrder.getStatus())) {
                    logger.info("payment order is not exist or already use..");
                    modelAndView.addObject("paymentOrder", paymentOrder);
                    modelAndView.addObject("result", "FAILED");
                    modelAndView.addObject("message", "支付单找不着或者已经使用。");
                    modelAndView.setViewName(ERROR_VIEW);
                    return modelAndView;
                }*/

                paymentOrder.setStatus(PaymentOrder.STATUS_SUCCESS);
                paymentOrderService.update(paymentOrder);
                modelAndView.addObject("paymentOrder", paymentOrder);
                modelAndView.setViewName(SUCCESS_VIEW);
                logger.info("payment order is updated, payment successfully.." );
            }else {
                modelAndView.addObject("paymentOrder", paymentOrder);
                modelAndView.addObject("result", "FAILED");
                modelAndView.addObject("message", "支付宝签名认证失败。");
                modelAndView.setViewName(ERROR_VIEW);
            }


        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("paymentOrder", paymentOrder);
            modelAndView.addObject("result", "FAILED");
            modelAndView.addObject("message", "系统错误");
            modelAndView.setViewName(ERROR_VIEW);
        }
        logger.debug("================= paymenr order value =================");
        logger.debug(paymentOrder.toString());
        logger.info("================= aplipay return notify end =================");
        return modelAndView;
    }

    @RequestMapping(value = "/wechat", method = RequestMethod.GET)
    public ModelAndView wechatReturnNotification(@RequestParam String id ,HttpServletRequest request, HttpServletResponse response) throws ServiceException, DataNotFoundException {
        logger.info("================= wechat return notify start =================");
        ModelAndView modelAndView = new ModelAndView();

       PaymentOrder paymentOrder = paymentOrderService.getById(id);
        if (paymentOrder == null) {
            paymentOrder = new PaymentOrder();
            modelAndView.addObject("paymentOrder", paymentOrder);
            modelAndView.addObject("result", "FAILED");
            modelAndView.addObject("message", "支付单找不着");
            modelAndView.setViewName(ERROR_VIEW);
            return modelAndView;
        }

        WechatPaymentService wechatPaymentService = new WechatPaymentServiceImpl();
        try {
            WechatPayResult payResult = wechatPaymentService.queryPayResult(paymentOrder.getOutTradeNo());
            if (WechatPayResult.SUCCESS.equalsIgnoreCase(payResult.getTradeState())) {
                paymentOrder.setStatus(PaymentOrder.STATUS_SUCCESS);
                paymentOrderService.update(paymentOrder);
                modelAndView.addObject("paymentOrder", paymentOrder);
                modelAndView.setViewName(SUCCESS_VIEW);
                return modelAndView;
            }

            modelAndView.addObject("paymentOrder", paymentOrder);
            modelAndView.addObject("result", "FAILED");
            modelAndView.addObject("message", "支付单不是真的支付成功");
            paymentOrder.setStatus(PaymentOrder.STATUS_FAIL);
            paymentOrderService.update(paymentOrder);

        } catch (WechatServiceException e) {
            modelAndView.addObject("paymentOrder", paymentOrder);
            modelAndView.addObject("result", "FAILED");
            modelAndView.addObject("message", "系统错误。");
            modelAndView.setViewName(ERROR_VIEW);
            return modelAndView;
        }

        return modelAndView;
    }

}
