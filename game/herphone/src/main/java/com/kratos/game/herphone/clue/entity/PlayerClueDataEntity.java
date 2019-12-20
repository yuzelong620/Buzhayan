package com.kratos.game.herphone.clue.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class PlayerClueDataEntity {

	@Id
	String id;
	long playerId; //玩家id
	int gameId; //剧本ID
	int chapterId; //章节ID
	Set<Integer> cluesIdList = new HashSet<>();//这个章节的所有线索id
	
	public PlayerClueDataEntity() {
		
	}
	public PlayerClueDataEntity(String id,long playerId, int gameId, int chapterId, Set<Integer> cluesIdList) {
		this.id = id;
		this.playerId = playerId;
		this.gameId = gameId;
		this.chapterId = chapterId;
		this.cluesIdList = cluesIdList;
	}
	
	
}
