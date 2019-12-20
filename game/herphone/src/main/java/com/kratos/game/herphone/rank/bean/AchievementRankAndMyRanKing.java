package com.kratos.game.herphone.rank.bean;

import java.util.List;

import lombok.Data;

@Data
public class AchievementRankAndMyRanKing {

	int myRanKing = -1; //自己的排名
	List<ResAchievementRank> resAchievementRankList;
	
	public AchievementRankAndMyRanKing() {
		super();
	}

	public AchievementRankAndMyRanKing(int myRanKing,List<ResAchievementRank> resAchievementRankList) {
		super();
		this.myRanKing = myRanKing;
		this.resAchievementRankList = resAchievementRankList;
	}
	
}
