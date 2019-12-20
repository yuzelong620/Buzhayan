package com.kratos.game.herphone.illegal.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class IllegalRecordEntity {

	@Id
	private String id;
	private long playerId;
	private int type; // 违规类型 1封号 2禁言
	private long create_time; // 违规时间
	private long bannedDuration; // 封禁时长

	public IllegalRecordEntity() {
	}

	public IllegalRecordEntity(String id, long playerId, int type, long create_time, long bannedDuration) {
		this.id = id;
		this.playerId = playerId;
		this.type = type;
		this.create_time = create_time;
		this.bannedDuration = bannedDuration;
	}

}
