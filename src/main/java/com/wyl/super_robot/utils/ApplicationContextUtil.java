package com.wyl.super_robot.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    @SuppressWarnings("static-access")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getBean(String className) throws BeansException, IllegalArgumentException {
        if (className == null || className.length() <= 0) {
            throw new IllegalArgumentException("className为空");
        }

        String beanName = null;
        if (className.length() > 1) {
            beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
        } else {
            beanName = className.toLowerCase();
        }
        return applicationContext != null ? applicationContext.getBean(beanName) : null;
    }

    public static Object getBean(Class clas) throws BeansException, IllegalArgumentException {
        return applicationContext != null ? applicationContext.getBean(clas) : null;
    }

}