package com.kratos.engine.framework.gm;

import com.kratos.engine.framework.gm.GmDispatcher;
import com.kratos.engine.framework.gm.GmHandler;
import com.kratos.engine.framework.net.socket.message.CmdExecutor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class GmHandlerBeanPostProcessor implements BeanPostProcessor, Ordered {

	private GmDispatcher gmDispatcher = GmDispatcher.getInstance();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		try {
			Class<?> clz = bean.getClass();
			Method[] methods = clz.getDeclaredMethods();
			for (Method method : methods) {
				GmHandler mapperAnnotation = method.getAnnotation(GmHandler.class);
				if (mapperAnnotation != null) {
					CmdExecutor cmdExecutor = CmdExecutor.valueOf(method, method.getParameterTypes(), bean);
					gmDispatcher.registerHandler(mapperAnnotation.cmd(), cmdExecutor);
				}
			}
		} catch (Exception e) {
			throw e;
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
