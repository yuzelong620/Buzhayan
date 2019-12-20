package com.kratos.game.herphone.systemMessgae.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class SystemMessgaeLastEntity {
	@Id
	private long playerId;
	private String playerName;
	private String content;
	private int sendType;  //0为系统给玩家发消息，1为玩家给系统发消息
	@Indexed
	private long createTime;	//发送消息时间
}
