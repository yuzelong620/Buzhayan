package com.kratos.engine.framework.event;

import com.google.common.eventbus.Subscribe;
import com.kratos.engine.framework.net.socket.annotation.MessageHandler;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Log4j
@Component
public class EventClassLoader implements BeanPostProcessor, Ordered {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            Class<?> clz = bean.getClass();
            Method[] methods = clz.getDeclaredMethods();
            for (Method method:methods) {
                Subscribe mapperAnnotation = method.getAnnotation(Subscribe.class);
                if(mapperAnnotation == null) {
                    continue;
                }
                OnFire.registerListener(bean);
            }

        }catch(Exception e) {
            log.error("", e);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
