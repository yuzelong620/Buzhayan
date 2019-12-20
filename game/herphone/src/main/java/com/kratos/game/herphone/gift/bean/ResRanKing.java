package com.kratos.game.herphone.gift.bean;

import java.util.List;

import lombok.Data;

@Data
public class ResRanKing {
	
	int myRanKing; //自己的排名
	String goodFeeling; //好感度
	List<RanKingBean> ranKingBeanList; //排行榜信息
	
	public ResRanKing() {
		
	}

	public ResRanKing(int myRanKing, String goodFeeling, List<RanKingBean> ranKingBeanList) {
		super();
		this.myRanKing = myRanKing;
		this.goodFeeling = goodFeeling;
		this.ranKingBeanList = ranKingBeanList;
	}
	
	
}
