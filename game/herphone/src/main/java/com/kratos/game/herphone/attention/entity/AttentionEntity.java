package com.kratos.game.herphone.attention.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 关注信息
 * 
 * @author
 *
 */
@Data
@Document
public class AttentionEntity {
	@Id
	private String id; // id
	@Indexed 
	private long fromPlayerId; // 关注的用户id
	private String fromPlayerNick; // 关注用户的名	
	private String fromPlayerAvatarUrl = "";// 关注者头像
	private String fromPlayerSignature="";//签名
	private int fromPlayerAchievementTags; //显示成就徽章
    private int fromPlayerAvatarFrame; //显示头像框
	
	@Indexed
	private long toPlayerId; // 被关注的用户id
	private String toPlayerNick = ""; // 被关注的用户名字
	private String toPlayerAvatarUrl = "";// 被关注者头像
	private String toPlayerSignature =""; //被关注者个性签名
	private int toPlayerAchievementTags; //显示成就徽章
    private int toPlayerAvatarFrame; //显示头像框
    
	public AttentionEntity() {
	}

	public AttentionEntity(String id, long fromPlayerId, String fromPlayerNick, long toPlayerId, String toPlayerNick,
			String toPlayerAvatarUrl, String fromPlayerAvatarUrl,String toPlayerSignature,String fromPlayerSignature,
			int fromPlayerAchievementTags,int fromPlayerAvatarFrame,int toPlayerAchievementTags,int toPlayerAvatarFrame) {
		this.id = id;
		this.fromPlayerId = fromPlayerId;
		this.fromPlayerNick = fromPlayerNick;
		this.fromPlayerAvatarUrl = fromPlayerAvatarUrl;
		this.toPlayerId = toPlayerId;
		this.toPlayerNick = toPlayerNick;
		this.toPlayerAvatarUrl = toPlayerAvatarUrl;
		this.toPlayerSignature = toPlayerSignature;
		this.fromPlayerSignature=fromPlayerSignature;
		this.fromPlayerAchievementTags = fromPlayerAchievementTags;
		this.fromPlayerAvatarFrame = fromPlayerAvatarFrame;
		this.toPlayerAchievementTags = toPlayerAchievementTags;
		this.toPlayerAvatarFrame = toPlayerAvatarFrame;
	}

}
