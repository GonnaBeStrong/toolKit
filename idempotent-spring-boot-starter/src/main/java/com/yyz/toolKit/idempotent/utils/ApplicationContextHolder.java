package com.yyz.toolKit.idempotent.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;

    }

    public static <T> T getBeanByType(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static Object getBeanByName(String name) {
        return applicationContext.getBean(name);
    }


    public static <T> T getBeanByNameAndType(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return applicationContext.findAnnotationOnBean(beanName, annotationType);
    }


}
