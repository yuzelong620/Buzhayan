package com.kratos.game.herphone.player.bean;

import lombok.Data;

@Data
public class PlayerAstrictBean {

	private String playerId;
	private String is_block;//是否封号
	private String no_speak_time;//禁言时间
	
	public PlayerAstrictBean() {
		
	}

	public PlayerAstrictBean(String playerId, String is_block, String no_speak_time) {
		this.playerId = playerId;
		this.is_block = is_block;
		this.no_speak_time = no_speak_time;
	}
	
}
