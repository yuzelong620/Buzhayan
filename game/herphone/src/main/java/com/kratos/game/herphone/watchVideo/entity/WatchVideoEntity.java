package com.kratos.game.herphone.watchVideo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document
public class WatchVideoEntity {
	@Id
	private long playerId;//用户id
	long lastWatchTime;//上次查看时间
	int totalNum;//总查看次数
	
	public WatchVideoEntity(long playerId ) {
		this.playerId = playerId;
	}
	public WatchVideoEntity() { 
	}
	
}
