package com.kratos.engine.framework.net.socket.message;

import com.kratos.engine.framework.net.socket.annotation.MessageMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper=true)
@MessageMeta
public class ErrorMessage extends Message {
	@NonNull
	private String message;

	public ErrorMessage() {

	}

	public ErrorMessage(String message) {
		this.message = message;
	}
}
