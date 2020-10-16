package com.yonyougov.http.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @ClassName ServerServiceDemoImpl
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/29 4:58 下午
 * @Version 1.0
 **/
@Component
@WebService(name = "ServerServiceDemo", targetNamespace = "http://webservice.http.yonyougov.com")
public class ServerServiceDemoImpl {
    private static Logger log = LoggerFactory.getLogger(ServerServiceDemoImpl.class);

    @WebMethod
    public String emrService(@WebParam String data) {
        if (null == data || "".equals(data.trim())) {
            return "传入的参数为空";
        }
        return "调用成功";
    }

}
