package com.kratos.engine.framework.net.socket.task;

import com.kratos.engine.framework.net.socket.IoSession;
import com.kratos.engine.framework.net.socket.SessionManager;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.engine.framework.net.socket.message.ErrorMessage;
import com.kratos.engine.framework.net.socket.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public final class CmdTask extends BaseTask {

    private static Logger logger = LoggerFactory.getLogger(CmdTask.class);

    private short module;
    private short cmd;
    /**
     * message controller
     */
    private Object handler;
    /**
     * target method of the controller
     */
    private Method method;
    /**
     * arguments passed to the method
     */
    private Object[] params;
    /**
     * owner session
     */
    private IoSession session;

    public static CmdTask valueOf(int dispatchMap, Object handler,
                                  Method method, Object[] params, IoSession session, short module, short cmd) {
        CmdTask msgTask = new CmdTask();
        msgTask.dispatchMap = dispatchMap;
        msgTask.handler = handler;
        msgTask.method = method;
        msgTask.params = params;
        msgTask.module = module;
        msgTask.cmd = cmd;
        msgTask.session = session;
        return msgTask;
    }

    @Override
    public void action() {
        try {
            method.invoke(handler, params);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof BusinessException) {
                session.sendPacket(new ErrorMessage(cause.getMessage()));
            } else {
                logger.error("message task execute failed ", cause);
            }
        }
    }

    public Object getHandler() {
        return handler;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getParams() {
        return params;
    }

    @Override
    public String toString() {
        return this.getName() + "[" + handler.getClass().getName() + "@" + method.getName() + "]";
    }

}
