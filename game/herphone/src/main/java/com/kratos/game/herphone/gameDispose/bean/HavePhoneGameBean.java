package com.kratos.game.herphone.gameDispose.bean;

import lombok.Data;

@Data
public class HavePhoneGameBean {

	String playerId;
	String gameName;
	String authorName;
	String phone;
	
	public HavePhoneGameBean() {
	}

	public HavePhoneGameBean(String playerId, String gameName, String authorName, String phone) {
		this.playerId = playerId;
		this.gameName = gameName;
		this.authorName = authorName;
		this.phone = phone;
	}
	
}
