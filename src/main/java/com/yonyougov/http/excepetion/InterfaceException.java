package com.yonyougov.http.excepetion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName InterfaceException
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/24 11:35 上午
 * @Version 1.0
 **/
public class InterfaceException extends RuntimeException {
    private static Logger log = LoggerFactory.getLogger(InterfaceException.class);

    private InterfaceException() {

    }

    protected InterfaceException(String msg) {
        super(msg);
    }

    public static InterfaceException getInstance(String msg) {
        return new InterfaceException(msg);
    }
}
