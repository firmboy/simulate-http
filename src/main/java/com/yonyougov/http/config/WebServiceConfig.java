package com.yonyougov.http.config;

import com.yonyougov.http.webservice.ServerServiceDemo1Impl;
import com.yonyougov.http.webservice.ServerServiceDemoImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @ClassName WebServiceConfig
 * @Description
 * @Author playboy
 * @Date 2020/9/29 5:00 下午
 * @Version 1.0
 **/
@Configuration
public class WebServiceConfig {
    private static Logger log = LoggerFactory.getLogger(WebServiceConfig.class);

    @Autowired
    private ServerServiceDemoImpl serverServiceDemo;

    @Autowired
    private ServerServiceDemo1Impl serverService1Demo;

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }


    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), serverServiceDemo);
        endpoint.publish("/ws/api");
        return endpoint;
    }

    @Bean
    public Endpoint endpoint1() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), serverService1Demo);
        endpoint.publish("/ws/api1");
        return endpoint;
    }
}
