package com.yonyougov.http.util;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName WebSocketUtil
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/24 7:46 下午
 * @Version 1.0
 **/
public class WebSocketUtil {
    private static Logger log = LoggerFactory.getLogger(WebSocketUtil.class);

    public static Map deal(HttpServletRequest request, HttpServletResponse response) {
        return Maps.newHashMap();
    }
}
