package com.kratos.game.herphone.systemMessgae.bean;

import lombok.Data;

@Data
public class ResSystemMessgae {
	private String playerId;		//玩家id
	private String content;		//消息内容
	private String fromUser;	//发送人名字
	private String sendTime;		//发送时间
	private String toUser;		//接收人名字
}
