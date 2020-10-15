package com.ciecc.common.workflow.utils;

import com.ciecc.common.reflect.ClassReflectUtils;
import com.ciecc.common.reflect.ReflectException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BeanUtils implements ApplicationContextAware {
    private static ApplicationContext ctx;

    private static BeanDefinitionRegistry beanDefinitionRegistry;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
        beanDefinitionRegistry = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
    }

    public static <T> T get(String name,Class<T> clazz){
        return ctx.getBean(name,clazz);
    }

    public static <T> T get(Class<T> clazz){
        return ctx.getBean(clazz);
    }

    public static Object get(String name){
        return ctx.getBean(name);
    }


    public static <T> T mapToEntity(Class<T> clazz, Map<?,?> mp){
        T instance = null;
        try {
            instance = ClassReflectUtils.getInstance(clazz);
            BeanMap bm =  BeanMap.create(instance);
            bm.putAll(mp);
        } catch (ReflectException e) {
            e.printStackTrace();
        }
        return instance;

    }

    public static <T> T mapToEntity(T instance,Map<?,?> mp){
        BeanMap bm = BeanMap.create(instance);
        bm.putAll(mp);
        return instance;

    }

    public static void register(String name,Class<?> obj){
        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(obj);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition(name,beanDefinition);
    }
}
