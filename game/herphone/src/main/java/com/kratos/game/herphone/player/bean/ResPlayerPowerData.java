package com.kratos.game.herphone.player.bean;

import lombok.Data;

@Data
public class ResPlayerPowerData {

	int power; // 电量
	int limit; // 上限

	public ResPlayerPowerData(int power, int limit) {
		super();
		this.power = power;
		this.limit = limit;
	} 

	public ResPlayerPowerData() {
		super();
	}

}
