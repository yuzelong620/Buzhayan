package com.kratos.game.herphone.achievement.bean;

import lombok.Data;

@Data
public class ResAchievementData {

	boolean getState;//是否获得
	int itemId; //获得的物品ID
	
	public ResAchievementData(boolean getState, int itemId) {
		super();
		this.getState = getState;
		this.itemId = itemId;
	}
	public ResAchievementData() {
		super();
	}

	
}
