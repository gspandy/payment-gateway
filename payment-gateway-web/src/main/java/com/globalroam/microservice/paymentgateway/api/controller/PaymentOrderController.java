package com.globalroam.microservice.paymentgateway.api.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.globalroam.microservice.paymentgateway.entity.EntityResponse;
import com.globalroam.microservice.paymentgateway.entity.PaymentOrder;
import com.globalroam.microservice.paymentgateway.exception.DataNotFoundException;
import com.globalroam.microservice.paymentgateway.exception.InvalidRequestException;
import com.globalroam.microservice.paymentgateway.exception.ServiceException;
import com.globalroam.microservice.paymentgateway.request.PaymentOrderModel;
import com.globalroam.microservice.paymentgateway.service.PaymentOrderService;
import com.joker.module.common.request.HttpUtil;
import com.joker.module.payment.alipay.AlipayConfig;
import com.joker.module.payment.wechat.config.WechatPaymentConfig;
import com.joker.module.payment.wechat.domain.WechatPrePayOrder;
import com.joker.module.payment.wechat.domain.WechatUserAuth;
import com.joker.module.payment.wechat.exception.WechatServiceException;
import com.joker.module.payment.wechat.service.WechatPaymentService;
import com.joker.module.payment.wechat.service.impl.WechatPaymentServiceImpl;
import com.joker.module.payment.wechat.util.CommonUtil;
import com.joker.module.payment.wechat.util.WechatPaymentUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Joker on 2017/4/29.
 */
@RestController
@RequestMapping(value = "/api/v1/paymentorder")
public class PaymentOrderController {


    private static Logger logger = LogManager.getLogger(PaymentOrderController.class);
    @Autowired
    private PaymentOrderService paymentOrderService;

    private WechatPaymentService wechatPaymentService;


    public PaymentOrderController() {
        this.wechatPaymentService = new WechatPaymentServiceImpl();
    }

    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    @ApiOperation(notes = "支付单ID必须传", httpMethod = "GET", value = "根据ID查询支付单")
    @ResponseBody
    public EntityResponse<PaymentOrder> getById(@PathVariable String id) throws ServiceException, DataNotFoundException {
        PaymentOrder paymentOrder = paymentOrderService.getById(id);
        EntityResponse<PaymentOrder> response = new EntityResponse<PaymentOrder>(paymentOrder);
        return response;
    }

    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PUT)
    @ApiOperation(notes = "支付单ID必须传", httpMethod = "PUT", value = "修改支付单状态，表示客户端已经使用。")
    @ResponseBody
    public EntityResponse<PaymentOrder> update(@PathVariable String id, @RequestParam String status) throws ServiceException, DataNotFoundException, InvalidRequestException {
        if (StringUtils.isEmpty(status) || !PaymentOrder.STATUS_COMPLETE.equalsIgnoreCase(status)) {
            throw new InvalidRequestException("100021", "请求参数不正确，只能为complete.");
        }
        PaymentOrder paymentOrder = paymentOrderService.getById(id);
        paymentOrder.setStatus(PaymentOrder.STATUS_COMPLETE);
        paymentOrderService.update(paymentOrder);
        EntityResponse<PaymentOrder> response = new EntityResponse<PaymentOrder>(paymentOrder);
        return response;
    }

    @RequestMapping(value = "", produces = {"application/json;charset=UTF-8"}, consumes = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    @ApiOperation(notes = "此API为创建支付单", httpMethod = "POST", value = "创建支付单")
    @ResponseBody
    public EntityResponse<PaymentOrder> create(@RequestBody PaymentOrderModel paymentOrderModel) throws ServiceException {
        PaymentOrder paymentOrder = new PaymentOrder(paymentOrderModel.getAmount(), paymentOrderModel.getTittle(), paymentOrderModel.getMethod(), PaymentOrder.STATUS_CREATE);
        paymentOrder.setCreateBy(paymentOrderModel.getUserId());
        PaymentOrder paymentOrderFromDB = paymentOrderService.add(paymentOrder);
        return new EntityResponse<PaymentOrder>(paymentOrderFromDB);
    }


    @RequestMapping(value = "/{id}/gateway", method = RequestMethod.GET)
    @ApiOperation(notes = "执行订单支付", httpMethod = "GET", value = "创建支付单")
    public ModelAndView gateway(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            PaymentOrder paymentOrder = paymentOrderService.getById(id);

            if (PaymentOrder.STATUS_COMPLETE.equals(paymentOrder.getStatus())) {
                //TODO 这里异常状态应该有UI提示
            }

            paymentOrder.setStatus(PaymentOrder.STATUS_PAY);
            paymentOrderService.update(paymentOrder);


            switch (paymentOrder.getMethod()) {
                case PaymentOrder.METHOD_ALIPAY_H5:
                    modelAndView.setViewName(alipayH5(paymentOrder, request, response));
                    return modelAndView;
                case PaymentOrder.METHOD_WECHAT_OPEN_H5:
                    modelAndView.setViewName(wechatOpenH5(paymentOrder, request, response));
                    return modelAndView;
                default:
                    modelAndView.setViewName(alipayH5(paymentOrder, request, response));
                    return modelAndView;
            }
        } catch (Exception e) {
            request.setAttribute("error", "调用支付接口失败");
            modelAndView.setViewName("error");
            return modelAndView;
        }

    }


    private String wechatH5(PaymentOrder paymentOrder, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    private String qrCode(PaymentOrder paymentOrder, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    private String wechatOpenH5(PaymentOrder paymentOrder, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        WechatUserAuth wechatUserAuth = (WechatUserAuth) request.getSession().getAttribute(WechatPaymentConfig.WECHAT_USER_AUTH_SESSION_KEY);

        if (wechatUserAuth == null) {
            String authUrl = HttpUtil.serviceBasePath(request) + WechatPaymentConfig.AUTH_URI;
            logger.debug("wechat open auth info is null, redirect to auth url :" + authUrl);
            response.sendRedirect(authUrl);
        }
        WechatPrePayOrder prePayOrder = null;

        try {
            int amount = Integer.parseInt(paymentOrder.getAmount() * 100 + "");
            prePayOrder = wechatPaymentService.generateOpenPrePayOrder(amount, paymentOrder.getTittle(), paymentOrder.getOutTradeNo(), wechatUserAuth.getOpenid(), WechatPaymentConfig.RETURN_URI, CommonUtil.getIpAddr(request));
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

        request.getRequestDispatcher("classpath:/templates/wechat-open-h5.ftl").forward(request, response);

        return null;
    }

    public String alipayH5(PaymentOrder paymentOrder, HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {

        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(paymentOrder.getOutTradeNo());
        model.setSubject(paymentOrder.getTittle());
        model.setTotalAmount(paymentOrder.getAmount() + "");
        model.setBody(paymentOrder.getDetail());
        model.setTimeoutExpress(AlipayConfig.TIMEOUT_EXPRESS);
        model.setProductCode(AlipayConfig.PRODUCT_CODE_QUICK_WAP_PAY); // 销售产品码 必填
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(AlipayConfig.NOTIFY_URL);
        // 设置同步地址
        alipay_request.setReturnUrl(AlipayConfig.RETURN_URL);

        // form表单生产
        String form = "";

        // 调用SDK生成表单
        form = client.pageExecute(alipay_request).getBody();
        response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();

        return null;
    }


}
