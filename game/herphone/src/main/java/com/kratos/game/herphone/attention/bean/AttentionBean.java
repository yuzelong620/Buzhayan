package com.kratos.game.herphone.attention.bean;

import lombok.Data;

@Data
public class AttentionBean {
	private String playerId;
	private String playerNick;
	private String headPic;
	private int isAttention;//我是否已经关注
	private String signature; //个性签名
	private int achievementTags; //显示成就徽章
    private int avatarFrame; //显示头像框
	
	public AttentionBean(long playerId, String playerNick, String headPic, int isAttention,String signature
			,int achievementTags,int avatarFrame) { 
		this.playerId = playerId+"";
		this.playerNick = playerNick;
		this.headPic = headPic;
		this.isAttention = isAttention;
		this.signature = signature;
		this.achievementTags = achievementTags;
		this.avatarFrame = avatarFrame;
	}
	public AttentionBean() { 
	}
	
}
