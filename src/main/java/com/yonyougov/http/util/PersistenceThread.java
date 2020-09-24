package com.yonyougov.http.util;

import com.yonyougov.http.entity.InterfaceNode;
import com.yonyougov.http.repo.InterfaceRepoPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName PersistenceThread
 * @Description 持久化线程
 * @Author playboy
 * @Date 2020/9/24 2:46 下午
 * @Version 1.0
 **/
public class PersistenceThread implements Runnable {
    private static Logger log = LoggerFactory.getLogger(PersistenceThread.class);

    private LinkedBlockingQueue<List<InterfaceNode>> queue;

    private InterfaceRepoPersistence interfaceRepoPersistence;

    PersistenceThread(LinkedBlockingQueue<List<InterfaceNode>> queue, InterfaceRepoPersistence interfaceRepoPersistence) {
        this.queue = queue;
        this.interfaceRepoPersistence = interfaceRepoPersistence;
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (queue) {
                    List<InterfaceNode> nodes = queue.take();
                    log.info("队列获取一个nodes");
                    interfaceRepoPersistence.persistence(nodes);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
