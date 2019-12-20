package com.kratos.engine.framework.net.socket.task;

import com.kratos.engine.framework.net.socket.IoSession;
import com.kratos.engine.framework.net.socket.SessionManager;
import com.kratos.engine.framework.net.socket.annotation.MessageHandler;
import com.kratos.engine.framework.net.socket.annotation.MessageMeta;
import com.kratos.engine.framework.net.socket.message.CmdExecutor;
import com.kratos.engine.framework.net.socket.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DefaultMessageDispatcher {
	public static DefaultMessageDispatcher instance = new DefaultMessageDispatcher();

	public static DefaultMessageDispatcher getInstance() {
		return instance;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	/** [module_cmd, CmdExecutor] */
	private static final Map<String, CmdExecutor> MODULE_CMD_HANDLERS = new HashMap<>();

	public void registerMethodInvoke(Object bean) {
		Class<?> clz = bean.getClass();
		Method[] methods = clz.getDeclaredMethods();
		for (Method method:methods) {
			MessageHandler mapperAnnotation = method.getAnnotation(MessageHandler.class);
			if (mapperAnnotation != null) {
				short[] meta = getMessageMeta(method);
				if (meta == null) {
					throw new RuntimeException(String.format("controller[%s] method[%s] lack of RequestMapping annotation",
							clz.getName(), method.getName()));
				}
				short module = meta[0];
				short cmd    = meta[1];
				String key = buildKey(module, cmd);

				CmdExecutor cmdExecutor = CmdExecutor.valueOf(method, method.getParameterTypes(), bean);

				registerMethodInvoke(key, cmdExecutor);
			}
		}
	}

	/**
	 * 返回方法所带Message参数的元信息
	 * @param method
	 * @return
	 */
	private short[] getMessageMeta(Method method) {
		for (Class<?> paramClazz: method.getParameterTypes()) {
			if (Message.class.isAssignableFrom(paramClazz)) {
				MessageMeta protocol = paramClazz.getAnnotation(MessageMeta.class);
				if (protocol != null) {
					short[] meta = {protocol.module(), protocol.cmd()};
					return meta;
				}
			}
		}
		return null;
	}

	public void registerMethodInvoke(String key, CmdExecutor executor) {
		if (MODULE_CMD_HANDLERS.containsKey(key)) {
			throw new RuntimeException(String.format("module[%s] duplicated", key));
		}
		MODULE_CMD_HANDLERS.put(key, executor);
	}

	/**
	 * message entrance, in which io thread dispatch messages
	 * @param session
	 * @param message
	 * @param module
	 * @param cmd
	 */
	public void dispatch(IoSession session, Message message, short module, short cmd) {

		CmdExecutor cmdExecutor = MODULE_CMD_HANDLERS.get(buildKey(module, cmd));
		if (cmdExecutor == null) {
			logger.error("message executor missed, module={},cmd={}", module, cmd);
			return;
		}

		Object[] params = convertToMethodParams(session, cmdExecutor.getParams(), message);
		Object controller = cmdExecutor.getHandler();

		GameExector.getInstance().acceptTask(
				CmdTask.valueOf((int) session.getPlayerId(),
						controller, cmdExecutor.getMethod(), params, session, module, cmd));
	}

	/**
	 * 将各种参数转为被RequestMapper注解的方法的实参
	 * @param session
	 * @param methodParams
	 * @param message
	 * @return
	 */
	private Object[] convertToMethodParams(IoSession session, Class<?>[] methodParams, Message message) {
		Object[] result = new Object[methodParams==null?0:methodParams.length];

		for (int i=0;i<result.length;i++) {
			Class<?> param = methodParams[i];
			if (IoSession.class.isAssignableFrom(param)) {
				result[i] = session;
			}else if (Long.class.isAssignableFrom(param)) {
				result[i] = session;
			}else if (long.class.isAssignableFrom(param)) {
				result[i] = SessionManager.getInstance().getPlayerIdBy(session);
			}else if (Message.class.isAssignableFrom(param)) {
				result[i] = message;
			}
		}

		return result;
	}

	private String buildKey(short module, short cmd) {
		return module + "_" + cmd;
	}

}
