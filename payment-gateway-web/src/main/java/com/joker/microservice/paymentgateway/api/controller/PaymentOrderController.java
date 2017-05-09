package com.joker.microservice.paymentgateway.api.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.joker.microservice.paymentgateway.cofig.GlobalConfigure;
import com.joker.microservice.paymentgateway.entity.EntityResponse;
import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import com.joker.microservice.paymentgateway.exception.*;
import com.joker.microservice.paymentgateway.exception.enums.ErrorInfo;
import com.joker.microservice.paymentgateway.exception.enums.PaymentOrderErrorInfo;
import com.joker.microservice.paymentgateway.request.AppPaymentOrderModel;
import com.joker.microservice.paymentgateway.request.PaymentOrderModel;
import com.joker.microservice.paymentgateway.response.APPPaymentOrder;
import com.joker.microservice.paymentgateway.service.PaymentOrderService;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    public EntityResponse<PaymentOrder> create(@RequestBody PaymentOrderModel paymentOrderModel) throws ServiceException, DataNotFoundException {
        PaymentOrder paymentOrder = new PaymentOrder(paymentOrderModel.getAmount(), paymentOrderModel.getTitle(), paymentOrderModel.getMethod(), PaymentOrder.STATUS_CREATE);
        paymentOrder.setCreateBy(paymentOrderModel.getUserId());
        paymentOrder.setOutTradeNo(paymentOrderModel.getOutTradeNo());
        paymentOrder.setReturnUrl(paymentOrderModel.getReturnUrl());
        paymentOrder.setNotifyUrl(paymentOrderModel.getNotifyUrl());
        paymentOrder.setCustom(paymentOrderModel.getCustom());

        PaymentOrder paymentOrderTemp = paymentOrderService.getByOutTradeNo(paymentOrder.getOutTradeNo());

        if (paymentOrderTemp != null) {
            throw new DataNotFoundException("100006", "该支付单商户单号已经存在");
        }
        PaymentOrder paymentOrderFromDB = paymentOrderService.add(paymentOrder);
        return new EntityResponse<PaymentOrder>(paymentOrderFromDB);
    }

    @RequestMapping(value = "/apptype", produces = {"application/json;charset=UTF-8"}, consumes = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    @ApiOperation(notes = "此API为APP创建支付单", httpMethod = "POST", value = "创建APP支付单")
    @ResponseBody
    public EntityResponse<APPPaymentOrder> createAppOrder(@Valid @RequestBody AppPaymentOrderModel paymentOrderModel, HttpServletRequest hsrequest, BindingResult result) throws ServiceException, DataNotFoundException, InvalidRequestException, InternalErrorException {

        PaymentOrder paymentOrder = new PaymentOrder(paymentOrderModel.getAmount(), paymentOrderModel.getTitle(), paymentOrderModel.getMethod(), PaymentOrder.STATUS_CREATE);
        paymentOrder.setCreateBy(paymentOrderModel.getUserId());
        paymentOrder.setOutTradeNo(paymentOrderModel.getOutTradeNo());
        paymentOrder.setNotifyUrl(paymentOrderModel.getNotifyUrl());
        paymentOrder.setReturnUrl(paymentOrderModel.getNotifyUrl());
        paymentOrder.setCustom(paymentOrderModel.getCustom());
        paymentOrder.setStatus(PaymentOrder.STATUS_CREATE);

        PaymentOrder paymentOrderTemp = paymentOrderService.getByOutTradeNo(paymentOrder.getOutTradeNo());

        if (paymentOrderTemp != null) {
            throw new InvalidRequestException("1000011", "该流水单号已经存在");
        }
        APPPaymentOrder appPaymentOrder = new APPPaymentOrder();

        switch (paymentOrder.getMethod()) {
            case PaymentOrder.METHOD_ALIPAY_APP:
                alipayAPP(hsrequest, paymentOrder, appPaymentOrder);
                break;
            default:
                throw new InvalidRequestException("100021", "APP支付方式目前只支持：" + PaymentOrder.METHOD_ALIPAY_APP);
        }


        return new EntityResponse<APPPaymentOrder>(appPaymentOrder);
    }

    private void alipayAPP(HttpServletRequest hsrequest, PaymentOrder paymentOrder, APPPaymentOrder appPaymentOrder) throws ServiceException, DataNotFoundException, InternalErrorException {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, "RSA2");
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(paymentOrder.getTitle());
        model.setSubject(paymentOrder.getTitle());
        model.setOutTradeNo(paymentOrder.getOutTradeNo());
        model.setTimeoutExpress(AlipayConfig.TIMEOUT_EXPRESS);
        model.setTotalAmount(paymentOrder.getAmount() + "");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(HttpUtil.serviceBasePath(hsrequest) + AlipayConfig.NOTIFY_URL);
        try {
            PaymentOrder paymentOrderFromDB = null;
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            paymentOrder.setTradeNo(response.getTradeNo());
            paymentOrderService.add(paymentOrder);
            paymentOrderFromDB = paymentOrderService.getById(paymentOrder.getId());
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            appPaymentOrder.setAmount(paymentOrder.getAmount());
            appPaymentOrder.setBody(response.getBody());
            appPaymentOrder.setCustom(paymentOrderFromDB.getCustom());
            appPaymentOrder.setStatus(paymentOrderFromDB.getStatus());
            appPaymentOrder.setTitle(paymentOrderFromDB.getTitle());
            appPaymentOrder.setOutTradeNo(paymentOrderFromDB.getOutTradeNo());
            appPaymentOrder.setMethod(paymentOrderFromDB.getMethod());
            appPaymentOrder.setUserId(paymentOrderFromDB.getCreateBy());
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new InternalErrorException("100012", "调用支付宝接口异常");
        }
    }


    @RequestMapping(value = "/{id}/gateway", method = RequestMethod.GET)
    @ApiOperation(notes = "执行支付网关支付", httpMethod = "GET", value = "执行支付网关支付")
    public ModelAndView gateway(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            PaymentOrder paymentOrder = paymentOrderService.getById(id);

            if (paymentOrder == null) {
                modelAndView.setViewName("error");
                modelAndView.addObject("result", "FAILED");
                modelAndView.addObject("message", "该支付单不存在");
                return modelAndView;
            }

            if (PaymentOrder.STATUS_COMPLETE.equals(paymentOrder.getStatus()) || PaymentOrder.STATUS_SUCCESS.equalsIgnoreCase(paymentOrder.getStatus())) {
                //TODO 这里异常状态应该有UI提示
                modelAndView.setViewName("error");
                modelAndView.addObject("paymentOrder", paymentOrder);
                modelAndView.addObject("result", "FAILED");
                modelAndView.addObject("message", "改订单已经支付过了");
                return modelAndView;
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
            e.printStackTrace();
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
            HttpUtil.setSessionAttribute(request, GlobalConfigure.SESSION_PAYMENT_ORDER_KEY, paymentOrder);
            response.sendRedirect(authUrl);
        }
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
        request.setAttribute("returnURL", HttpUtil.serviceBasePath(request) + WechatPaymentConfig.RETURN_URI);

        return "wechat-open-h5";
    }

    public String alipayH5(PaymentOrder paymentOrder, HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {

        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(paymentOrder.getOutTradeNo());
        model.setSubject(paymentOrder.getTitle());
        model.setTotalAmount(paymentOrder.getAmount() + "");
        model.setBody(paymentOrder.getDetail());
        model.setTimeoutExpress(AlipayConfig.TIMEOUT_EXPRESS);
        model.setProductCode(AlipayConfig.PRODUCT_CODE_QUICK_WAP_PAY); // 销售产品码 必填
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(HttpUtil.serviceBasePath(request) + AlipayConfig.NOTIFY_URL);
        // 设置同步地址
        alipay_request.setReturnUrl(HttpUtil.serviceBasePath(request) + AlipayConfig.RETURN_URL);

        // form表单生产
        String form = "";

        // 调用SDK生成表单
        form = client.pageExecute(alipay_request).getBody();

        request.setAttribute("form", form);
        return "alipay-h5";
    }


}
