package com.kratos.engine.framework.event;

/**
 * 事件基类
 * 
 * @author herton
 *
 */
public interface BaseEvent {
	
	default Object getOwner() {
		return String.valueOf("system");
	}

}
