package com.joker.microservice.paymentgateway.exception.handler;


import com.joker.microservice.paymentgateway.entity.APIResponse;
import com.joker.microservice.paymentgateway.exception.DataNotFoundException;
import com.joker.microservice.paymentgateway.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @Author zhangjian
 * @Date 2017/2/13
 * @Copyright:
 * @Describe:
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandler extends Throwable {


    public GlobalExceptionHandler() {
    }


    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object handleInvalidRequestError(InvalidRequestException ex) {
        return new APIResponse(ex.getCode(), ex.getMessage());
    }


    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Object handleDataNotFoundException(DataNotFoundException ex) {
        return new APIResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Object handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ex.printStackTrace();
        return new APIResponse("404", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleUnexpectedServerError(RuntimeException ex) {
        ex.printStackTrace();
        return new APIResponse("500", "internal server error : " + ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object defaultErrorHandler(Exception e) throws Exception {
        e.printStackTrace();
        return new APIResponse("500", "internal server error : " + e.getMessage());
    }

}
