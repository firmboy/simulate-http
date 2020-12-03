package com.yonyougov.http.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName InputStreamFilter
 * @Description
 * @Author playboy
 * @Date 2020/12/3 3:05 下午
 * @Version 1.0
 **/
@WebFilter(filterName = "inputStreamFilter", urlPatterns = {"/*"})
@Order(FilterRegistrationBean.LOWEST_PRECEDENCE)
public class InputStreamFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(InputStreamFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = new BufferedServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(httpRequest, response);
    }
}
