package com.kratos.game.herphone.clue.bean;

import lombok.Data;

@Data
public class ReqClueData {
	
	int gameId; //剧本ID
	int chapterId; //章节ID
	int clueId; //线索id
	
	public ReqClueData() {
		
	}
	
	public ReqClueData(int gameId, int chapterId) {
		this.gameId = gameId;
		this.chapterId = chapterId;
	}
	
	public ReqClueData(int gameId, int chapterId, int clueId) {
		this.gameId = gameId;
		this.chapterId = chapterId;
		this.clueId = clueId;
	}
}
