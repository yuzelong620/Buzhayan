package com.kratos.game.herphone.helperMessage.bean;

import lombok.Data;

@Data
public class ReqOptionAnswer {

	private int videoAwardId = -1; //视频ID
	
	public ReqOptionAnswer() {
	}

	public ReqOptionAnswer(int videoAwardId) {
		super();
		this.videoAwardId = videoAwardId;
	}
}
