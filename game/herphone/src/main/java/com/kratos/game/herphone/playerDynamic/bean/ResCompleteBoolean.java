package com.kratos.game.herphone.playerDynamic.bean;

import lombok.Data;

@Data
public class ResCompleteBoolean {
	private boolean register;
	private boolean continuouLogin;
	private boolean noSpeakTime;
	
	public ResCompleteBoolean() {		
		this.register = false;
		this.continuouLogin = false;
		this.noSpeakTime = true;
	}
	
}
