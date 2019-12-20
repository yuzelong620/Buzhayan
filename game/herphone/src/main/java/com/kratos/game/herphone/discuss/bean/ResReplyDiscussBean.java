package com.kratos.game.herphone.discuss.bean;

import lombok.Data;

@Data
public class ResReplyDiscussBean {
	String myDiscussId;//我的评论id
	int gameId;//游戏id
	int chapterId;//章节id
	String myContent;//我的评论内容
	int readState;//是否已读   0：未读  1：已读
	String fromPlayerId;//来自评论的玩家id
	String fromNickName;//来自评论的玩家昵称
	String fromHeadImgUrl;//来自评论的玩家头像
	String fromContent;//来自评论的内容
	String fromDiscussId;//来自评论的评论id
	long replyTime;//回复评论时间
	int avatarFrame;//回复玩家头像框
    int achievementTags;//回复玩家徽章
	public ResReplyDiscussBean() {
		super();
	}

	
	
}
