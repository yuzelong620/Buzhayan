package com.kratos.game.herphone.dynamic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/** 评论点赞实体类 */
@Data
@Document
public class DynamicLikeEntity {
	@Id
	String id;// 拼接，player_discussId
	long playerId;// 角色id
	String dynamicId;// 评论id

	public DynamicLikeEntity(String id, long playerId, String dynamicId) {
		this.id = id;
		this.playerId = playerId;
		this.dynamicId = dynamicId;
	}

	public DynamicLikeEntity() {
	}

}
