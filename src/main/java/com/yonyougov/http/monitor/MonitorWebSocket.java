package com.yonyougov.http.monitor;

import com.google.common.collect.Lists;
import com.yonyougov.http.repo.MonitorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName MonitorWebSocket
 * @Description
 * @Author playboy
 * @Date 2020/9/24 3:31 下午
 * @Version 1.0
 **/
@Component
@ServerEndpoint("/monitor/{interId}")
public class MonitorWebSocket {
    private static Logger log = LoggerFactory.getLogger(MonitorWebSocket.class);

    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 当前会话session
     */
    @OnOpen
    public void onOpen(@PathParam("interId") String interId, Session session) {
        this.session = session;
        MonitorRepo.wsClientMap.add(this);
        CopyOnWriteArrayList<MonitorWebSocket> monitorWebSockets = MonitorRepo.clientKeyMap.get(interId);
        if (ObjectUtils.isEmpty(monitorWebSockets)) {
            monitorWebSockets = Lists.newCopyOnWriteArrayList();
            MonitorRepo.clientKeyMap.put(interId, monitorWebSockets);
        }
        monitorWebSockets.add(this);
        //addOnlineCount();
        log.info(session.getId() + "有新链接加入，当前链接数为：" + MonitorRepo.wsClientMap.size());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        MonitorRepo.wsClientMap.remove(this);
        //subOnlineCount();
        log.info("有一链接关闭，当前链接数为：" + MonitorRepo.wsClientMap.size());
    }

    /**
     * 收到客户端消息
     *
     * @param message 客户端发送过来的消息
     * @param session 当前会话session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("来终端的警情消息:" + message);
        //sendMsgToAll(message);
    }

    /**
     * 发生错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("wsClientMap发生错误!");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        log.info("成功发送一条消息:" + message);
    }


}
