package com.yonyougov.http.registered;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yonyougov.http.entity.InterfaceNode;
import com.yonyougov.http.excepetion.InterfaceException;
import com.yonyougov.http.monitor.MonitorWebSocket;
import com.yonyougov.http.repo.InterfaceRepo;
import com.yonyougov.http.repo.MonitorRepo;
import com.yonyougov.http.util.RegisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName RegisService
 * @Description 注册接口service
 * @Author playboy
 * @Date 2020/9/23 2:55 下午
 * @Version 1.0
 **/
@Service
public class RegisService {
    private static Logger log = LoggerFactory.getLogger(RegisService.class);


    public InterfaceNode save(InterfaceNode node) {
        String id = node.getId();
        InterfaceNode interfaceNode = InterfaceRepo.idMpas.get(id);
        if (ObjectUtils.isEmpty(interfaceNode)) {
            //第一次保存
            InterfaceRepo.idMpas.put(id, node);
            String pId = node.getpId();
            if (ObjectUtils.isEmpty(pId)) {
                //顶级节点
                InterfaceRepo.nodes.add(node);
            } else {
                InterfaceNode pNode = InterfaceRepo.idMpas.get(pId);
                List<InterfaceNode> children = pNode.getChildren();
                if (ObjectUtils.isEmpty(children)) {
                    children = Lists.newArrayList();
                    pNode.setChildren(children);
                }
                children.add(node);
            }
        }
        try {
            InterfaceRepo.queue.put(InterfaceRepo.nodes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return node;
    }

    public InterfaceNode registered(InterfaceNode node) {
        //先判断结果是否能为json字符串
        String result = node.getResult();
        if (node.getResultType().equals("json")) {
            try {
                JSONObject.parseObject(result, HashMap.class);
            } catch (Exception e) {
                throw InterfaceException.getInstance("返回值json格式不正确");
            }
        }
        //判断接口是否有重复，没有的话
        InterfaceNode addrNode = InterfaceRepo.interMaps.get(node.getAddr());
        if (!ObjectUtils.isEmpty(addrNode)) {
            if (!addrNode.getId().equals(node.getId())) {
                throw InterfaceException.getInstance("该接口地址已经存在，请勿重复注册");
            } else {
                if (addrNode.getHasRegis()) {
                    throw InterfaceException.getInstance("该接口地址已注册");
                }
            }
        }
        InterfaceNode interfaceNode = InterfaceRepo.idMpas.get(node.getId());
        fill(node, interfaceNode);
        InterfaceRepo.interMaps.put(node.getAddr(), interfaceNode);
        RegisUtil.regis(interfaceNode);
        try {
            InterfaceRepo.queue.put(InterfaceRepo.nodes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return interfaceNode;
    }

    public InterfaceNode unregistered(InterfaceNode node) {
        //判断接口是否在被监听
        CopyOnWriteArrayList<MonitorWebSocket> monitorWebSockets =
                MonitorRepo.clientKeyMap.get(node.getId());
        if (!ObjectUtils.isEmpty(monitorWebSockets)) {
            throw InterfaceException.getInstance("该接口正在被监听，请先停止监听");
        }
        //如果没有被监听，直接取消注册
        InterfaceNode interfaceNode = InterfaceRepo.idMpas.get(node.getId());
        fill(node, interfaceNode);
        RegisUtil.unregis(interfaceNode);
        try {
            InterfaceRepo.queue.put(InterfaceRepo.nodes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return interfaceNode;
    }

    public void fill(InterfaceNode source, InterfaceNode target) {
        target.setAddr(source.getAddr());
        target.setResult(source.getResult());
        target.setText(source.getText());
        target.setHasRegis(source.getHasRegis());
        target.setResultType(source.getResultType());
    }

    public void delete(InterfaceNode node) {
        String pId = node.getpId();
        InterfaceNode interfaceNode = InterfaceRepo.idMpas.get(node.getId());
        if (!ObjectUtils.isEmpty(pId)) {
            InterfaceNode pNode = InterfaceRepo.idMpas.get(pId);
            List<InterfaceNode> children = pNode.getChildren();
            children.remove(interfaceNode);
        } else {
            InterfaceRepo.nodes.remove(interfaceNode);
        }
        deleteNode(node);
        try {
            InterfaceRepo.queue.put(InterfaceRepo.nodes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteNode(InterfaceNode node) {
        if (!ObjectUtils.isEmpty(node.getAddr())) {
            CopyOnWriteArrayList<MonitorWebSocket> monitorWebSockets =
                    MonitorRepo.clientKeyMap.get(node.getId());
            if (!ObjectUtils.isEmpty(monitorWebSockets)) {
                throw InterfaceException.getInstance("接口正在被监听，请先停止监听");
            }
            InterfaceRepo.interMaps.remove(node.getAddr());
        }
        InterfaceRepo.idMpas.remove(node.getId());
        List<InterfaceNode> children = node.getChildren();
        if (!ObjectUtils.isEmpty(children)) {
            for (InterfaceNode child : children) {
                deleteNode(child);
            }
        }
    }


}
