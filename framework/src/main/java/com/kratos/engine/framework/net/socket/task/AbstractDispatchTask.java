package com.kratos.engine.framework.net.socket.task;

import lombok.extern.log4j.Log4j;

@Log4j
public abstract class AbstractDispatchTask implements IDispatchTask {

	public String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * 业务执行前后的触发点
	 */
	public void run() {
		action();
	}

}
