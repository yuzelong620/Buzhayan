package com.kratos.engine.framework.gm.message;

import com.kratos.engine.framework.net.Modules;
import com.kratos.engine.framework.net.socket.annotation.MessageMeta;
import com.kratos.engine.framework.net.socket.message.Message;

@MessageMeta(module = Modules.GM, cmd = 1)
public class ReqGmCommand extends Message {
	
	private String params;

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "ReqGmCommand [params=" + params + "]";
	}
	

}
