package com.kratos.game.herphone.clue.bean;

import lombok.Data;

@Data
public class ClueAddRes {
	int addClueValue;//增加了多少 线索值
	int currentClueValue;//玩家当前 线索
	public ClueAddRes(int addClueValue, int currentClueValue){
		this.addClueValue = addClueValue;
		this.currentClueValue = currentClueValue;
	}
	public ClueAddRes(){
	}
}
