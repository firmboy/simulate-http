package com.yonyougov.http.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @ClassName DynamicBeanHandler
 * @Description TODO
 * @Author playboy
 * @Date 2020/9/30 6:26 下午
 * @Version 1.0
 **/
@Component
public class DynamicBeanHandler implements ApplicationContextAware {
    private static Logger log = LoggerFactory.getLogger(DynamicBeanHandler.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 将targetBean中的T类型的属性替换成sourceFieldBean
     *
     * @param targetBean
     * @param sourceFieldBean
     * @param t
     * @param <T>
     */
    public <T> void changeBeanField(String targetBean, String sourceFieldBean, T t) {
        Object bean = applicationContext.getBean(targetBean);
        if (bean == null) {
            return;
        }

        //1
        Class<?> type = applicationContext.getType(targetBean);
        if (type == null) {
            return;
        }
        Field[] declaredFields = type.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            if (fieldType == t.getClass()) {
                try {
                    field.set(bean, t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        //2
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(t.getClass());

        defaultListableBeanFactory.registerBeanDefinition("", beanDefinitionBuilder.getRawBeanDefinition());

    }
}
