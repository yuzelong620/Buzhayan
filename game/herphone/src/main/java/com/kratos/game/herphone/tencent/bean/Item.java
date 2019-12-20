package com.kratos.game.herphone.tencent.bean;

import lombok.Data;

@Data
public class Item {
	String openid;
	String unionid;
	long playerId;

	public Item(String openid, String unionid,long playerId) {
		this.openid = openid;
		this.unionid = unionid;
		this.playerId=playerId;
	}

}
