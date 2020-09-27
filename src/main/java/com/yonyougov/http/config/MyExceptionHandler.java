package com.yonyougov.http.config;

import com.yonyougov.http.entity.Response;
import com.yonyougov.http.excepetion.InterfaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName MyExceptionHandler
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/27 11:58 上午
 * @Version 1.0
 **/
@ControllerAdvice
public class MyExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response exceptionHandler(Exception e) {
        if (e instanceof InterfaceException) {
            return Response.failReponse(e.getMessage());
        } else {
            return Response.failReponse("内部异常");
        }
    }
}
