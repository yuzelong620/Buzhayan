package com.kratos.game.herphone.gift.bean;

import lombok.Data;

@Data
public class GiftRanKing {

	long PlayerId; //id
	String nickName; //昵称
	String headImgUrl; //头像
	String sex;	 //性别 1男 2女0外星人
	long favoraBility; //好感度
	
	
	public GiftRanKing() {
	}
	
	public GiftRanKing(long playerId, String nickName, String headImgUrl, String sex,long favoraBility) {
		PlayerId = playerId;
		this.nickName = nickName;
		this.headImgUrl = headImgUrl;
		this.sex = sex;
		this.favoraBility = favoraBility;
	}
	
}
