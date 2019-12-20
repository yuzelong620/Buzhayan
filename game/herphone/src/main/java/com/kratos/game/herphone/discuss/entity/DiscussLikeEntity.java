package com.kratos.game.herphone.discuss.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/** 评论点赞实体类 */
@Data
@Document
public class DiscussLikeEntity {
	@Id
	String id;// 拼接，player_discussId
	long playerId;// 角色id
	String discussId;// 评论id
	long likeTime;//点赞时间
	long toPlayerId;// 被点赞玩家Id
	int isRead;//被点赞玩家是否阅读该消息;  1:已读 ,0:未读
	public DiscussLikeEntity(String id, long playerId, String discussId) {
		this.id = id;
		this.playerId = playerId;
		this.discussId = discussId;
	}
	
	
	public DiscussLikeEntity() {
	}


	public DiscussLikeEntity(String id, long playerId, String discussId, long likeTime, long toPlayerId, int isRead) {
		super();
		this.id = id;
		this.playerId = playerId;
		this.discussId = discussId;
		this.likeTime = likeTime;
		this.toPlayerId = toPlayerId;
		this.isRead = isRead;
	}

}
