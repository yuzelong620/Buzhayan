package com.kratos.engine.framework.net.socket.task;

public interface IDispatchTask extends IDispatch {
	
	String getName();
	
	/** 
	 * 业务执行
	 */
	void action();
	
	/**
	 * 业务执行前后的触发点
	 */
	void run();

}
