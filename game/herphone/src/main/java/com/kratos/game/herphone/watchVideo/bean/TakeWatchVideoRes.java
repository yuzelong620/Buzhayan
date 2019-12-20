package com.kratos.game.herphone.watchVideo.bean;

import lombok.Data;

@Data
public class TakeWatchVideoRes {
	
	public TakeWatchVideoRes() { 
	}

	public TakeWatchVideoRes(int currentPower) { 
		this.currentPower = currentPower;
	}

	int currentPower;//当前体力 
}
