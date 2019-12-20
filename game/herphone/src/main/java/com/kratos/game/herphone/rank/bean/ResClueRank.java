package com.kratos.game.herphone.rank.bean;

import com.kratos.game.herphone.rank.entity.CluesRankEntity;

import lombok.Data;

@Data
public class ResClueRank {
	 private String playerId;
	 private String nickName;
	 private String avatarUrl;
	 private String signature;
	 private int rank;
	 private int value;
	 private int achievementTags; //显示成就徽章
	 private int avatarFrame; //显示头像框
	 
	public ResClueRank(CluesRankEntity cluesRankEntity) {
		super();
		this.playerId = String.valueOf(cluesRankEntity.getPlayerDynamicEntity().getPlayerId());
		this.nickName = cluesRankEntity.getPlayerDynamicEntity().getNickName();
		this.avatarUrl = cluesRankEntity.getPlayerDynamicEntity().getHeadImgUrl();
		this.signature = cluesRankEntity.getPlayerDynamicEntity().getSignature();
		this.rank = cluesRankEntity.getId();
		this.value = cluesRankEntity.getPlayerDynamicEntity().getClue();
		this.achievementTags = cluesRankEntity.getPlayerDynamicEntity().getAchievementTags();
		this.avatarFrame = cluesRankEntity.getPlayerDynamicEntity().getAvatarFrame();
	}
	public ResClueRank() {
		super();
	}
}
