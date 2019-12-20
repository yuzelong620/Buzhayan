package com.kratos.game.herphone.rank.bean;

import com.kratos.game.herphone.rank.entity.AchievementRankEntity;


import lombok.Data;

@Data
public class ResAchievementRank {
	 private String playerId;
	 private String nickName;
	 private String avatarUrl;
	 private String signature;
	 private int rank;
	 private int value;
	 private int achievementTags; //显示成就徽章
	 private int avatarFrame; //显示头像框
	 
	 
	public ResAchievementRank(AchievementRankEntity achievementRankEntity) {
		super();
		this.playerId = String.valueOf(achievementRankEntity.getPlayerDynamicEntity().getPlayerId());
		this.nickName = achievementRankEntity.getPlayerDynamicEntity().getNickName();
		this.avatarUrl = achievementRankEntity.getPlayerDynamicEntity().getHeadImgUrl();
		this.signature = achievementRankEntity.getPlayerDynamicEntity().getSignature();
		this.rank = achievementRankEntity.getId();
		this.value = achievementRankEntity.getPlayerDynamicEntity().getAchievementDebris();
		this.achievementTags = achievementRankEntity.getPlayerDynamicEntity().getAchievementTags();
		this.avatarFrame = achievementRankEntity.getPlayerDynamicEntity().getAvatarFrame();
	}
	public ResAchievementRank() {
		super();
	}
}
