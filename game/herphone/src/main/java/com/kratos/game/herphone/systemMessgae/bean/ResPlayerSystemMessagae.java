package com.kratos.game.herphone.systemMessgae.bean;

import com.kratos.game.herphone.systemMessgae.entity.SystemMessgaeEntity;

import lombok.Data;

@Data
public class ResPlayerSystemMessagae {

	private String id;
	private String playerId;
	private String nickname;
	private int readState;	//未读状态  0未读，1已读
	private int sendType;  //0为系统给玩家发消息，1为玩家给系统发消息
	private int messageType;	//0为文字，1为链接，2图片
	private String content;		//发送内容
	private long createTime;		
	
	public ResPlayerSystemMessagae(SystemMessgaeEntity systemMessgaeEntity) {
		super();
		this.id = systemMessgaeEntity.getId();
		this.playerId = String.valueOf(systemMessgaeEntity.getPlayerId());
		this.nickname = systemMessgaeEntity.getNickname();
		this.readState = systemMessgaeEntity.getReadState();
		this.sendType = systemMessgaeEntity.getSendType();
		this.messageType = systemMessgaeEntity.getContentType();
		this.content = systemMessgaeEntity.getContent();
		this.createTime = systemMessgaeEntity.getCreateTime();
	}
	
	
}
