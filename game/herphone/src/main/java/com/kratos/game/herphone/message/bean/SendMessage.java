package com.kratos.game.herphone.message.bean;

import lombok.Data;

@Data
public class SendMessage {
	long toPlayerId;
	String content;
	int messageType;
	int audioTime;
}
