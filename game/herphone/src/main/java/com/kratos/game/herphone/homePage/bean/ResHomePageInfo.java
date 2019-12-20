package com.kratos.game.herphone.homePage.bean;

import java.util.List;

import lombok.Data;
@Data
public class ResHomePageInfo {
	
	private int moneyNum;
	
	private int power;
	
	private int powerLimit;
	
	private List<GamePageNum> gamePageNum;

	public ResHomePageInfo() {
		super();
	}

	public ResHomePageInfo(int moneyNum,int power, int powerLimit,List<GamePageNum> gamePageNum) {
		super();
		this.moneyNum = moneyNum;
		this.power = power;
		this.powerLimit = powerLimit;
		this.gamePageNum = gamePageNum;
	}
	
	
}
