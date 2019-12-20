package com.kratos.game.herphone.task.bean;

import lombok.Data;

@Data
public class EveryDayVideoBean {

	private Integer videoAwardId;
	private String videoUrl;
	private Integer manyDays;
	private String picture; //图片
	
	public EveryDayVideoBean() {
	}

	public EveryDayVideoBean(Integer videoAwardId, String videoUrl, Integer manyDays,String picture) {
		this.videoAwardId = videoAwardId;
		this.videoUrl = videoUrl;
		this.manyDays = manyDays;
		this.picture = picture;
	}
}
