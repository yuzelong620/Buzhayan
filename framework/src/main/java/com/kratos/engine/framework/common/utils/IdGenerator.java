package com.kratos.engine.framework.common.utils;

import com.kratos.engine.framework.ServerConfig;
import com.kratos.engine.framework.base.SpringContext;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 分布式id生成器
 * @author herton
 */
public class IdGenerator {

	private static AtomicLong generator = new AtomicLong(0);

	/**
	 * 生成全局唯一id
	 */
	public static long getNextId() {
		//----------------id格式 -------------------------
		//----------long类型8个字节64个比特位----------------
		// 高16位          	| 中32位          |  低16位
		// serverId        系统秒数          自增长号

		long serverId = (long) SpringContext.getBean(ServerConfig.class).getServerId();
		return  (serverId << 48)
			  |	(((System.currentTimeMillis() / 1000)) << 16)
			  | (generator.getAndIncrement() & 0xFFFF);
	}

	/**
	 * 生成临时id
	 */
	public static long getNextTempId() {
		return  (((System.currentTimeMillis() / 1000)) << 16)
				| (generator.getAndIncrement() & 0xFFFF);
	}
}
