package com.yonyougov.http.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @ClassName MappingRegulator
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/23 7:50 下午
 * @Version 1.0
 **/
public class MappingRegulator {
    private static Logger log = LoggerFactory.getLogger(MappingRegulator.class);


    public static void controlCenter(Class<?> controllerClass, ApplicationContext Context, Integer type) throws IllegalAccessException, Exception {
        //获取RequestMappingHandlerMapping
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) Context.getBean("requestMappingHandlerMapping");
        Method getMappingForMethod = ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getMappingForMethod", Method.class, Class.class);
        //设置私有属性为可见
        getMappingForMethod.setAccessible(true);
        //获取类中的方法
        Method[] method_arr = controllerClass.getMethods();
        for (Method method : method_arr) {
            //判断方法上是否有注解RequestMapping
            if (method.getAnnotation(RequestMapping.class) != null) {
                //获取到类的RequestMappingInfo
                RequestMappingInfo mappingInfo = (RequestMappingInfo) getMappingForMethod.invoke(requestMappingHandlerMapping, method, controllerClass);
                if (type == 1) {
                    //注册
                    registerMapping(requestMappingHandlerMapping, mappingInfo, controllerClass, method);
                } else if (type == 2) {
                    //取消注册
                    unRegisterMapping(requestMappingHandlerMapping, mappingInfo);
                    registerMapping(requestMappingHandlerMapping, mappingInfo, controllerClass, method);
                } else if (type == 3) {
                    unRegisterMapping(requestMappingHandlerMapping, mappingInfo);
                }

            }
        }
    }

    public static void registerMapping(RequestMappingHandlerMapping requestMappingHandlerMapping, RequestMappingInfo mappingInfo, Class<?> controllerClass, Method method) throws Exception, IllegalAccessException {
        requestMappingHandlerMapping.registerMapping(mappingInfo, controllerClass.newInstance(), method);
    }

    public static void unRegisterMapping(RequestMappingHandlerMapping requestMappingHandlerMapping, RequestMappingInfo mappingInfo) throws Exception, IllegalAccessException {
        requestMappingHandlerMapping.unregisterMapping(mappingInfo);
    }
}
