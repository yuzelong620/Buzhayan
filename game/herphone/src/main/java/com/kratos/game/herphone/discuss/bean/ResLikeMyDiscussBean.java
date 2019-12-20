package com.kratos.game.herphone.discuss.bean;

import lombok.Data;

@Data
public class ResLikeMyDiscussBean {
	String discussId;
	String content;
	long likeTime;
	int gameId;
	int chapterId;
	String fromPlayerId;
	String nickName;
	String headImgUrl;
	int isRead;
	int avatarFrame;//点赞玩家头像框
    int achievementTags;//点赞玩家徽章
	public ResLikeMyDiscussBean() {
		super();
	}
	
	
}
