package com.kratos.game.herphone.player.bean;

import lombok.Data;

@Data
public class PlayerPage {

	public Integer start; //第几条开始
	public Integer page; //每页多少条
	
	public PlayerPage() {
	}

	public PlayerPage(int start, int page) {
		this.start = start;
		this.page = page;
	}
}
