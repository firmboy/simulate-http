package com.yonyougov.http.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yonyougov.http.entity.InterfaceNode;
import com.yonyougov.http.entity.WebSocketMessage;
import com.yonyougov.http.monitor.MonitorWebSocket;
import com.yonyougov.http.repo.InterfaceRepo;
import com.yonyougov.http.repo.MonitorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
        String requestURI = request.getRequestURI();
        
        InterfaceNode interfaceNode = InterfaceRepo.interMaps.get(requestURI);
        if (!ObjectUtils.isEmpty(interfaceNode)) {
            StringBuffer sb = new StringBuffer();
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String[] value = entry.getValue();
                for (String s : value) {
                    sb.append(entry.getKey() + "=" + s + "&");
                }
            }
            String url = "";
            if (sb.length() > 0) {
                url = sb.substring(0, sb.length() - 1);
            }
            WebSocketMessage webSocketMessage = new WebSocketMessage();
            webSocketMessage.setUrl(ObjectUtils.isEmpty(url) ? requestURI : requestURI + "?" + url);
            String body = getBody(request);
            webSocketMessage.setBody(body);
            webSocketMessage.setResult(interfaceNode.getResult());
            webSocketMessage.setTime(DateUtil.now());
            sendMessage(interfaceNode.getId(), webSocketMessage);
            String result = interfaceNode.getResult();
            return JSONObject.parseObject(result, HashMap.class);
        } else {
            Map map = Maps.newHashMap();
            map.put("msg", "没有匹配到对应的接口");
            return map;
        }
    }

    public static String getBody(HttpServletRequest request) {
        BufferedReader br = null;
        String body = "";
        //1.获取request中的body
        try {
            br = request.getReader();
            String str = "";
            while ((str = br.readLine()) != null) {
                body += str;
            }
        } catch (IOException e) {
            //
        }
        return body;
    }

    public static void sendMessage(String interId, WebSocketMessage webSocketMessage) {
        CopyOnWriteArrayList<MonitorWebSocket> monitorWebSockets = MonitorRepo.clientKeyMap.get(interId);
        if (!ObjectUtils.isEmpty(monitorWebSockets)) {
            for (MonitorWebSocket monitorWebSocket : monitorWebSockets) {
                try {
                    monitorWebSocket.sendMessage(JSONObject.toJSONString(webSocketMessage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
