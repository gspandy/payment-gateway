package com.joker.microservice.paymentgateway.service.impl;

import com.joker.microservice.paymentgateway.entity.PaymentOrder;
import com.joker.microservice.paymentgateway.exception.DataNotFoundException;
import com.joker.microservice.paymentgateway.exception.ServiceException;
import com.joker.microservice.paymentgateway.mapper.PaymentOrderMapper;
import com.joker.microservice.paymentgateway.service.PaymentOrderService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Joker on 2017/4/29.
 */
@Service
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private static Logger logger = LogManager.getLogger(PaymentOrderServiceImpl.class);


    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Override
    public PaymentOrder getById(String id) throws ServiceException, DataNotFoundException {
        logger.info("======查询支付单开始=======");
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setId(id);
        paymentOrder = paymentOrderMapper.selectByPrimaryKey(paymentOrder);
        logger.info("======查询支付单结束=======");
        return paymentOrder;
    }

    @Override
    public PaymentOrder add(PaymentOrder paymentOrder) throws ServiceException {
        logger.info("======创建支付单开始=======");

        if (StringUtils.isEmpty(paymentOrder.getTitle())) {
            throw new ServiceException("100004", "支付单标题为空，请填写该参数");
        }

        if (StringUtils.isEmpty(paymentOrder.getNotifyUrl())) {
            throw new ServiceException("100007", "notify_url 为空，请填写该参数");
        }

        if (StringUtils.isEmpty(paymentOrder.getReturnUrl())) {
            throw new ServiceException("10008", "return_url为空，请填写该参数");
        }

        if (paymentOrder.getAmount() <= 0) {
            throw new ServiceException("100005", "支付额度必须大于0");
        }
        if (!PaymentOrder.isValidMethod(paymentOrder.getMethod())) {
            throw new ServiceException("100003", "支付方式不正确，暂时不支持该选项");
        }
        paymentOrder.setStatus(PaymentOrder.STATUS_CREATE);
        //  paymentOrder.setOutTradeNo(DateUtil.timeStampUUID(null));
        int count = paymentOrderMapper.insert(paymentOrder);
        if (count <= 0) {
            throw new ServiceException("100002", "添加支付单失败，请联系管理员");
        }
        PaymentOrder paymentOrderFromDB = paymentOrderMapper.selectByPrimaryKey(paymentOrder);
        logger.info("======创建支付单结束=======");
        return paymentOrderFromDB;
    }

    @Override
    public PaymentOrder getByAmountAndOutTradeNo(String outTradeNo, double amount) throws ServiceException, DataNotFoundException {
        logger.info("======根据商户单号查询支付单开始=======");
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setOutTradeNo(outTradeNo);
        paymentOrder.setAmount(amount);
        paymentOrder = paymentOrderMapper.selectOne(paymentOrder);
        logger.info("======根据商户单号查询支付单结束=======");
        return paymentOrder;
    }

    @Override
    public PaymentOrder getByOutTradeNo(String outTradeNo) {
        logger.info("======根据商户单号查询支付单开始=======");
        Example example = new Example(PaymentOrder.class);
        example.createCriteria().andEqualTo("outTradeNo", outTradeNo);
        List<PaymentOrder> orders = paymentOrderMapper.selectByExample(example);
        logger.info("======根据商户单号查询支付单结束=======");
        return orders != null && orders.size() > 0 ? orders.get(0) : null;
    }

    @Override
    public PaymentOrder update(PaymentOrder paymentOrder) throws ServiceException {
        logger.info("======更新支付单开始=======");

        int rst = paymentOrderMapper.updateByPrimaryKey(paymentOrder);

        PaymentOrder paymentOrderFromDB = paymentOrderMapper.selectByPrimaryKey(paymentOrder);
        logger.info("======更新支付单结束=======");
        return paymentOrderFromDB;
    }

}
