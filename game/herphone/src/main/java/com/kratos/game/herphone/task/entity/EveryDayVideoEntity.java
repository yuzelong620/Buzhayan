package com.kratos.game.herphone.task.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class EveryDayVideoEntity {

	@Id
	private long playerId;
	private int videoAwardId;
	private Integer nowManyDays; //第几天
	private long nowDate; //当前时间 
	
	public EveryDayVideoEntity() {
	} 
	
	public EveryDayVideoEntity(long playerId, int videoAwardId, Integer nowManyDays, long nowDate) {
		this.playerId = playerId;
		this.videoAwardId = videoAwardId;
		this.nowManyDays = nowManyDays;
		this.nowDate = nowDate;
	}
}
