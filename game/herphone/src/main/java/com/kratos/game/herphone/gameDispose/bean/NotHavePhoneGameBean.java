package com.kratos.game.herphone.gameDispose.bean;

import lombok.Data;

@Data
public class NotHavePhoneGameBean {

	String playerId;
	String gameName;
	String authorName;
	
	public NotHavePhoneGameBean() {
	}

	public NotHavePhoneGameBean(String playerId, String gameName, String authorName) {
		this.playerId = playerId;
		this.gameName = gameName;
		this.authorName = authorName;
	}

}