package com.yonyougov.http.repo;

import com.google.common.collect.Maps;
import com.yonyougov.http.monitor.MonitorWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName MonitorRepo
 * @Description 监听的内存中的数据
 * @Author playboy
 * @Date 2020/9/24 3:33 下午
 * @Version 1.0
 **/
public class MonitorRepo {
    private static Logger log = LoggerFactory.getLogger(MonitorRepo.class);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    public static CopyOnWriteArraySet<MonitorWebSocket> wsClientMap = new CopyOnWriteArraySet<>();

    public static ConcurrentMap<String, CopyOnWriteArrayList<MonitorWebSocket>> clientKeyMap = Maps.newConcurrentMap();
}
