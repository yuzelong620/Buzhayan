package com.kratos.game.herphone.score.message;

import lombok.Data;

@Data
public class ResScoreInfo {
	private Integer score ;
	private int state = 0;
		
	public ResScoreInfo(Integer score, int state) {
		super();
		this.score = score;
		this.state = state;
	}

	public ResScoreInfo() {
		super();
	}
	
}
