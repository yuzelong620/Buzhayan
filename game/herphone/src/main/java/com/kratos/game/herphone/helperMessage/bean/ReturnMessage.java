package com.kratos.game.herphone.helperMessage.bean;

import lombok.Data;

@Data
public class ReturnMessage {

	private Integer videoAwardId;
	private String videoUrl;
	private int videoWatchState = 0; //0默认未领取 1领取
	private Integer manyDays;
	private String picture; //图片
	
	public ReturnMessage() {
	}
	
	public ReturnMessage(Integer videoAwardId, String videoUrl, int videoWatchState,Integer manyDays,String picture) {
		this.videoAwardId = videoAwardId;
		this.videoUrl = videoUrl;
		this.videoWatchState = videoWatchState;
		this.manyDays = manyDays;
		this.picture = picture;
	}
	
	
	
}
