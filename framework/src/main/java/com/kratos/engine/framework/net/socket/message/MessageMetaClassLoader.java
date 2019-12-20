package com.kratos.engine.framework.net.socket.message;

import com.kratos.engine.framework.net.socket.task.DefaultMessageDispatcher;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class MessageMetaClassLoader implements BeanPostProcessor, Ordered {

	private DefaultMessageDispatcher messageDispatcher = DefaultMessageDispatcher.getInstance();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		//扫描所有controller的消息处理器
		try {
			MessageFactory.getInstance().registerClass(bean.getClass());
		}catch(Exception e) {
			log.error("", e);
		}

		return bean;
	}


	private String buildKey(short module, short cmd) {
		return module + "_" + cmd;
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
