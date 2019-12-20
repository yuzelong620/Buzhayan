package com.kratos.game.herphone.gameOver.bean;

import lombok.Data;

@Data
public class ResGameOverInfo {
	//玩家玩过的剧本数
	int scenarioNum;
	//线索值
	int clue;
	//奖励内容
	String award ="";
	
	public ResGameOverInfo() {
		super();
	}
	
	
}
