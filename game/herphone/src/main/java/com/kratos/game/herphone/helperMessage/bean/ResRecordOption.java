package com.kratos.game.herphone.helperMessage.bean;

import lombok.Data;

@Data
public class ResRecordOption {

	int power; //当前玩家电量

	public ResRecordOption() {
	}
	
	public ResRecordOption(int power) {
		this.power = power;
	}
}
