package com.globalroam.microservice.paymentgateway.web.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.globalroam.microservice.paymentgateway.entity.PaymentOrder;
import com.globalroam.microservice.paymentgateway.service.PaymentOrderService;
import com.joker.module.payment.alipay.AlipayConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
    public ModelAndView alipayNotify(HttpServletRequest request,HttpServletResponse response) {

        logger.info("================= aplipay return notify start =================");
        ModelAndView modelAndView = new ModelAndView();
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


            //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            //计算得出通知验证结果
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);

            if (verify_result) {//验证成功
                logger.info("sign veridy is success.");
                PaymentOrder paymentOrder = paymentOrderService.getByOutTradeNo(outTradeNo);

                if (paymentOrder == null || PaymentOrder.STATUS_SUCCESS.equalsIgnoreCase(paymentOrder.getStatus()) || PaymentOrder.STATUS_COMPLETE.equalsIgnoreCase(paymentOrder.getStatus())) {
                    logger.info("payment order is already use..");
                    modelAndView.setViewName(ERROR_VIEW);
                    return modelAndView;
                }

                paymentOrder.setStatus(PaymentOrder.STATUS_SUCCESS);
                paymentOrder.setTradeNo(tradeNo);
                paymentOrderService.update(paymentOrder);
                modelAndView.setViewName(SUCCESS_VIEW);
                logger.info("payment order is updated, payment successfully..");
            }else {
                modelAndView.setViewName(ERROR_VIEW);
            }


        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName(ERROR_VIEW);
        }
        logger.info("================= aplipay return notify start =================");
        return modelAndView;
    }
}
