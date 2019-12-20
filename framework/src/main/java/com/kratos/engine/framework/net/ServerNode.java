package com.kratos.engine.framework.net;

/**
 * 
 * 各类对外服务节点
 * @author herton
 *
 */
public interface ServerNode {

	/**
	 * 服务初始化
	 * @param port 端口
	 * @param heartbeatTick 心跳频率，秒
	 */
	void init(int port, int heartbeatTick);
	
	/**
	 *  服务启动
	 * @throws Exception
	 */
	void start() throws Exception;
	
	/**
	 * 服务关闭
	 * @throws Exception
	 */
	void shutDown() throws Exception;
	

}
