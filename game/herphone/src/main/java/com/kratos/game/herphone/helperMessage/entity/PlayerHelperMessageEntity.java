package com.kratos.game.herphone.helperMessage.entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class PlayerHelperMessageEntity {

	@Id
	private long playerId;
	private int videoAwardId;
	private int videoWatchState = 0; //视频观看完成状态 0未完成观看 1完成观看
	private Integer nowManyDays; //第几天
	private long nowDate; //当前时间 
	private List<Integer> videoAwardIds; 
	
	public PlayerHelperMessageEntity() {
	}

	public PlayerHelperMessageEntity(long playerId, int videoAwardId, int videoWatchState, Integer nowManyDays,
			long nowDate, List<Integer> videoAwardIds) {
		this.playerId = playerId;
		this.videoAwardId = videoAwardId;
		this.videoWatchState = videoWatchState;
		this.nowManyDays = nowManyDays;
		this.nowDate = nowDate;
		this.videoAwardIds = videoAwardIds;
	}
	
}
