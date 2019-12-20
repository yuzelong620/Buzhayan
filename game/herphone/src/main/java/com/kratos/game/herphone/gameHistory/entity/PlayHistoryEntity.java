package com.kratos.game.herphone.gameHistory.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class PlayHistoryEntity {
	@Id
	private Integer gameId;
	
	private Integer showNum;

	public PlayHistoryEntity() {
		super();
	}
	
	
}
