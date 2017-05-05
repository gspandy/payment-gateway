package com.joker.module.payment.wechat.service;

import com.joker.module.payment.wechat.domain.WechatPayResult;
import com.joker.module.payment.wechat.domain.WechatUserAuth;
import com.joker.module.payment.wechat.exception.WechatServiceException;
import com.joker.module.payment.wechat.domain.WechatPrePayOrder;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhangjian on 2016/8/9.
 */
public interface WechatPaymentService {

    /**
     * 获取code的url，
     * @param returnUrl 用户处理后会被微信重定向的返回链接 "http://zhangjian.iok.la/wechat/test/openid" 链接需要配置到微信公众号
     * @return 这里需要将客户端重定向到返回的url上
     */
    String generateGetCodeURL(String returnUrl)  throws WechatServiceException;

    /**
     * 用code获取用户的认证相关信息，
     * @param code
     * @return 这里的返回的WechatUserAuth 中的token先关暂时先可以保存。这个阶段只用到openid
     */
    WechatUserAuth getWechatUserAuth(String code) throws WechatServiceException;

    /**
     *
     * 生成预支付订单
     * @param mount 总金额，单位为分 例如1元 等于 100分
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param openid 获取的用户openid
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @return 返回创建预支付订单结果
     */
    WechatPrePayOrder generateOpenPrePayOrder(int mount, String tittle, String outTradeNo, String openid, String notifyUrl, String ip)  throws WechatServiceException;


    /**
     *
     * 生成预支付订单
     * @param mount 总金额，单位为分 例如1元 等于 100分
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param attach 自定义字段传递
     * @param openid 获取的用户openid
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @return 返回创建预支付订单结果
     */
    WechatPrePayOrder generateOpenPrePayOrder(int mount, String tittle, String outTradeNo,String attach, String openid, String notifyUrl, String ip)  throws WechatServiceException;



    /**
     *
     * 生成预支付订单 链接形式，用于二维码支付
     * @param mount 总金额，单位为分 列入1元 等于 100分
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param productId 此id为二维码中包含的商品ID，商户自行定义。
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param attach 自定义字段传递
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @return 返回创建预支付订单结果
     */
    WechatPrePayOrder generateURLPrePayOrder(int mount, String tittle,String productId, String outTradeNo,String attach,String notifyUrl, String ip) throws WechatServiceException;

    /**
     *
     * 生成预支付订单 链接形式，用于二维码支付
     * @param mount 总金额，单位为分 列入1元 等于 100分
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param productId 此id为二维码中包含的商品ID，商户自行定义。
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param attach 自定义字段传递
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @return 返回创建预支付订单结果
     */
    WechatPrePayOrder generateMWEBPrePayOrder(int mount, String tittle,String productId, String outTradeNo,String attach,String notifyUrl, String ip) throws WechatServiceException;


    /**
     *
     * 生成预支付订单 链接形式，用于二维码支付
     * @param mount 总金额，单位为分 列入1元 等于 100分
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param productId 此id为二维码中包含的商品ID，商户自行定义。
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @return 返回创建预支付订单结果
     */
    WechatPrePayOrder generateURLPrePayOrder(int mount, String tittle,String productId, String outTradeNo,String notifyUrl, String ip) throws WechatServiceException;


    /**
     *
     * @param xmlData 微信支付结果调用接口返回的数据
     * @return 返回通知接口对象
     * @throws WechatServiceException
     */
    WechatPayResult parseNotifyXMLData(String xmlData) throws WechatServiceException;

    /**
     * 将微信扫码长连接转换为短连接
     * @param longURL 需要转换的长链接
     * @return 短连接
     */
    String transferLong2ShortURL(String longURL) throws WechatServiceException;

    /**
     *
     * 生成预支付订单 链接形式，用于二维码支付
     * @param mount 总金额，单位为分 列入1元 等于 100分
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param productId 此id为二维码中包含的商品ID，商户自行定义。
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @param outputStream 二维码输出流
     * @return 返回创建预支付订单结果
     */
    void generateQRCodePrePayOrder(int mount, String tittle, String productId, String outTradeNo, String notifyUrl, String ip, OutputStream outputStream) throws WechatServiceException;

    /**
     *
     * 生成预支付订单 链接形式，用于二维码支付
     * @param mount 总金额，单位为分 列入1元 等于 100分
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param productId 此id为二维码中包含的商品ID，商户自行定义。
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @param qrCodeOutPut 二维码输出流
     * @param logoInput 二维码logo输入流
     * @return 返回创建预支付订单结果
     */
    void generateQRCodePrePayOrder(int mount, String tittle, String productId, String outTradeNo, String notifyUrl, String ip, OutputStream qrCodeOutPut,InputStream logoInput) throws WechatServiceException;


    /**
     *
     * 生成预支付订单 链接形式，用于二维码支付
     * @param mount 总金额，单位为分 列入1元 等于 100分
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param productId 此id为二维码中包含的商品ID，商户自行定义。
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @param qrCodeOutPut 二维码输出流
     * @param width 二维码宽
     * @param height 二维码高
     * @return
     */
    void generateQRCodePrePayOrder(int mount, String tittle, String productId, String outTradeNo, String notifyUrl, String ip, OutputStream qrCodeOutPut,int width,int height) throws WechatServiceException;

    /**
     *
     * 生成预支付订单 链接形式，用于二维码支付
     * @param mount 总金额，单位为分 列入1元 等于 100分
     * @param tittle 订单的描述信息，比如商品购买或者商品充值
     * @param productId 此id为二维码中包含的商品ID，商户自行定义。
     * @param outTradeNo 订单号：20160808174447（32个字符内，可包含字母）
     * @param notifyUrl 交易成功异步通知接口
     * @param ip 微信客户端ip
     * @param qrCodeOutPut 二维码输出流
     * @param logoInput 二维码中间logo
     * @param width 二维码宽
     * @param height 二维码高
     * @return
     */
    void generateQRCodePrePayOrder(int mount, String tittle, String productId, String outTradeNo, String notifyUrl, String ip, OutputStream qrCodeOutPut,InputStream logoInput,int width,int height) throws WechatServiceException;


    /**
     * 根据商户单号查询微信单信息
     * @param outTradeNo 商户单号
     * @return
     * @throws WechatServiceException
     */
    WechatPayResult queryPayResult(String outTradeNo) throws WechatServiceException;

    /**
     * 解析查询支付单结果
     * @param xmlData
     * @return
     * @throws WechatServiceException
     */
     WechatPayResult parseQueryOrderXMLData(String xmlData) throws WechatServiceException;
}
