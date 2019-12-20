package com.kratos.game.herphone.gameHistory.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Document
@Data
public class GameHistoryEntity {
	@Id
	private long playerId; //玩家Id
	private Map<Integer, Long> gameHistory = new HashMap<Integer, Long>();//玩家玩的历史副本
	
	public GameHistoryEntity() {
		super();
	}
	
	
}
