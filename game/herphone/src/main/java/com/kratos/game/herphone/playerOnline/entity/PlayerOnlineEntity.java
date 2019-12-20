package com.kratos.game.herphone.playerOnline.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data 
@Document
public class PlayerOnlineEntity {

	@Id
	private long playerId;
	private long onlineTime;	//在线时间
	private long lastTime;		//上次心跳请求时间

	public PlayerOnlineEntity() {
	}

	public PlayerOnlineEntity(long playerId) {
		this.playerId = playerId;
		this.onlineTime = 0;
		this.lastTime = System.currentTimeMillis();
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}


}
