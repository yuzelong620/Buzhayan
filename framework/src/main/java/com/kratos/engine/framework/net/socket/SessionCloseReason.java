package com.kratos.engine.framework.net.socket;

public enum SessionCloseReason {
	
	/** 正常退出 */
	NORMAL,
	
	/** 链接超时 */
	OVER_TIME,

	/** token 不正确 */
	INVALID_TOKEN,


}
