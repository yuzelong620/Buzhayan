package com.kratos.game.herphone.message.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
@Data
@Document
public class MessageEntity {
	public MessageEntity() {
	}
	public MessageEntity(String id, int state, long createTime, String content, long fromPlayerId, String fromNickName,
			String fromAvatarUrl, long toPlayerId, String toNickName, String toAvatarUrl,int messageType,
			int fromAchievementTags,int fromAvatarFrame,int toAchievementTags,int toAvatarFrame) { 
		this.id = id;
		this.state = state;
		this.createTime = createTime;
		this.content = content;
		this.fromPlayerId = fromPlayerId;
		this.fromNickName = fromNickName;
		this.fromAvatarUrl = fromAvatarUrl;
		this.toPlayerId = toPlayerId;
		this.toNickName = toNickName;
		this.toAvatarUrl = toAvatarUrl;
		this.messageType=messageType;
		this.fromAchievementTags = fromAchievementTags;
		this.fromAvatarFrame = fromAvatarFrame;
		this.toAchievementTags = toAchievementTags;
		this.toAvatarFrame = toAvatarFrame;
	}
	public MessageEntity(String id, int state, long createTime, String content, long fromPlayerId, String fromNickName,
			String fromAvatarUrl, long toPlayerId, String toNickName, String toAvatarUrl,int messageType,int audioTime,
			int fromAchievementTags,int fromAvatarFrame,int toAchievementTags,int toAvatarFrame) { 
		this.id = id;
		this.state = state;
		this.createTime = createTime;
		this.content = content;
		this.fromPlayerId = fromPlayerId;
		this.fromNickName = fromNickName;
		this.fromAvatarUrl = fromAvatarUrl;
		this.toPlayerId = toPlayerId;
		this.toNickName = toNickName;
		this.toAvatarUrl = toAvatarUrl;
		this.messageType=messageType;
		this.audioTime = audioTime;
		this.fromAchievementTags = fromAchievementTags;
		this.fromAvatarFrame = fromAvatarFrame;
		this.toAchievementTags = toAchievementTags;
		this.toAvatarFrame = toAvatarFrame;
	}
	@Id
	private String id;
	@Indexed
	int state;//0未读，1已读
	@Indexed
	long createTime;
	
	int messageType;//资源类型，默认0==文本， 1语音  2图片 3视频 
	
	String content = ""; 
	
	@Indexed
	long fromPlayerId; 
	String fromNickName = ""; 
	String fromAvatarUrl = ""; 
	int fromAchievementTags;
	int fromAvatarFrame;
	
	@Indexed
	long toPlayerId; 
	String toNickName = ""; 
	String toAvatarUrl = ""; 
	int audioTime; 
	int toAchievementTags;
	int toAvatarFrame;
}
