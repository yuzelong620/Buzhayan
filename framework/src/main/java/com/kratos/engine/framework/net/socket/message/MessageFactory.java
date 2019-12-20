package com.kratos.engine.framework.net.socket.message;

import com.kratos.engine.framework.gm.facade.GmFacade;
import com.kratos.engine.framework.net.socket.annotation.MessageMeta;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class  MessageFactory {

	private static MessageFactory instance = new MessageFactory();

	private Map<Integer, Class<? extends Message>> id2Clazz = new HashMap<>();

	private Map<Class<?>, Integer> clazz2Id = new HashMap<>();

	public static MessageFactory getInstance() {
		return instance;
	}

	public void init() {
	}

	@SuppressWarnings("unchecked")
	public void registerClass(Class<?> clazz) {
		if (!Message.class.isAssignableFrom(clazz)) {
			return;
		}
		MessageMeta protocol = clazz.getAnnotation(MessageMeta.class);
		if (protocol == null) {
			return;
		}
		short module = protocol.module();
		short cmd = protocol.cmd();
		Integer key = buildKey(module, cmd);
		if (id2Clazz.containsKey(key)) {
			throw new RuntimeException("message meta [" + key + "] duplicate！！");
		}
		clazz2Id.put(clazz, key);
		id2Clazz.put(key, (Class<? extends Message>) clazz);
	}

	/**
	 * 返回消息的模板class
	 */
	public Class<? extends Message> getMessageMeta(short module, short cmd) {
		Integer key = buildKey(module, cmd);
		return getMessageMeta(key);
	}

	private Class<? extends Message> getMessageMeta(Integer key) {
		return id2Clazz.get(key);
	}

	public int getMessageId(Class<?> clazz) {
		return clazz2Id.get(clazz);
	}
	
	private int buildKey(short module, short cmd) {
		return module * 10000 + cmd;
	}
}
