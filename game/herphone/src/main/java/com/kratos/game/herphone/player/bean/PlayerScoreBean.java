package com.kratos.game.herphone.player.bean;

import lombok.Data;

@Data
public class PlayerScoreBean {

	private String scoreName; //剧本名称
	private double score; //剧本平均分
	private Integer playerNum; //评论玩家人数
	
	public PlayerScoreBean() {
	}
	
	public PlayerScoreBean(String scoreName, double score, Integer playerNum) {
		super();
		this.scoreName = scoreName;
		this.score = score;
		this.playerNum = playerNum;
	}
	
}
