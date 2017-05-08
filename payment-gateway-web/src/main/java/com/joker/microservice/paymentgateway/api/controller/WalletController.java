package com.joker.microservice.paymentgateway.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

/**
 * @Author zhangjian
 * @Date 2017/3/21
 * @Copyright:
 * @Describe:
 */
//@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @RequestMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    @ApiOperation(notes = "walletId必传参数", httpMethod = "GET", value = "根据钱包ID以及卡片ID卡片")
    @ApiResponses(value = {@ApiResponse(code = 403, message = "请求参数错误，ID为空"),
            @ApiResponse(code = 404, message = "该ID钱包卡片不存在"),
            @ApiResponse(code = 500, message = "系统内部错误")})
    public Object wallet(){
        return new HashMap<String, String>();
    }


    @RequestMapping(value = "", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    @ApiOperation(notes = "获取所钱包", httpMethod = "GET", value = "根据钱包ID以及条件查询钱包")
    @ApiResponses(value = {@ApiResponse(code = 403, message = "请求参数错误，ID为空"),
            @ApiResponse(code = 404, message = "卡片不存在"),
            @ApiResponse(code = 500, message = "系统内部错误")})
    public Object wallets(){
        return new HashMap<String, String>();
    }
}
