package com.kratos.game.herphone.fandom.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/** 评论点赞实体类 */
@Data
@Deprecated
@Document
public class FandomLikeEntity {
	@Id
	String id;// 拼接，player_discussId
	long playerId;// 角色id
	String fandomId;// 评论id

	public FandomLikeEntity(String id, long playerId, String fandomId) {
		this.id = id;
		this.playerId = playerId;
		this.fandomId = fandomId;
	}

	public FandomLikeEntity() {
	}

}
