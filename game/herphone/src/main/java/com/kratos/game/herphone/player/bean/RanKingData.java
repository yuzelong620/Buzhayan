package com.kratos.game.herphone.player.bean;

import lombok.Data;

@Data
public class RanKingData {
	
	private String id; //玩家id
	private String avatar_url;
	private String role_id; //角色id
	private String nick_name; //角色昵称
	private String topScore; //角色排行分数
	private String phone; //手机号码
	
	public RanKingData() {
	}
	public RanKingData(String id, String avatar_url, String role_id, String nick_name, String topScore) {
		this.id = id;
		this.avatar_url = avatar_url;
		this.role_id = role_id;
		this.nick_name = nick_name;
		this.topScore = topScore;
	}

	public RanKingData(String id, String avatar_url, String role_id, String nick_name, String topScore,String phone) {
		super();
		this.id = id;
		this.avatar_url = avatar_url;
		this.role_id = role_id;
		this.nick_name = nick_name;
		this.topScore = topScore;
		this.phone = phone;
	}

}
