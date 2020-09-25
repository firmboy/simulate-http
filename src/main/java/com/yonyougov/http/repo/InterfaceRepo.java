package com.yonyougov.http.repo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yonyougov.http.entity.InterfaceNode;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName InterfaceRepo
 * @Description 存储接口的实体
 * @Author playboy
 * @Date 2020/9/23 2:47 下午
 * @Version 1.0
 **/
public class InterfaceRepo {

    //存储接口信息
    public static List<InterfaceNode> nodes = Lists.newArrayList();

    //接口和节点的对应关系，防止出现重复接口
    public static Map<String, InterfaceNode> interMaps;

    //id和节点的对应关系
    public static Map<String, InterfaceNode> idMpas = Maps.newConcurrentMap();

    public static AtomicInteger count = new AtomicInteger(0);

    //前后端交互的队列
    public static LinkedBlockingQueue<List<InterfaceNode>> queue = new LinkedBlockingQueue(10);

    static {
        if (ObjectUtils.isEmpty(interMaps)) {
            interMaps = Maps.newConcurrentMap();
        }
    }
}
