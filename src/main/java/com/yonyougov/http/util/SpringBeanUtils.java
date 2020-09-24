package com.yonyougov.http.util;

import com.yonyougov.http.repo.InterfaceRepo;
import com.yonyougov.http.repo.InterfaceRepoPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName SpringBeanUtils
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/24 10:23 上午
 * @Version 1.0
 **/
@Component
public class SpringBeanUtils implements ApplicationContextAware {
    private static Logger log = LoggerFactory.getLogger(SpringBeanUtils.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.applicationContext = applicationContext;
        InterfaceRepoPersistence interfaceRepoPersistence = applicationContext.getBean(InterfaceRepoPersistence.class);
        //开启持久化数据的线程
        Thread thread = new Thread(new PersistenceThread(InterfaceRepo.queue, interfaceRepoPersistence));
        thread.setName("持久化线程");
        log.info("持久化线程启动");
        thread.start();

        //将磁盘数据加载到内存
        interfaceRepoPersistence.load();

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
