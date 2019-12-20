package com.kratos.game.herphone.gift.entity;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class GiftEntity {

	@Id
	String id;
	long sendGiftPlayerId; //送礼物的玩家ID
	long authorId;//作者ID
	long goodFeeling; //对这个作者的好感度总数
	
	public GiftEntity() {
	}

	public GiftEntity(String id, long sendGiftPlayerId, long authorId, long goodFeeling) {
		this.id = id;
		this.sendGiftPlayerId = sendGiftPlayerId;
		this.authorId = authorId;
		this.goodFeeling = goodFeeling;
	}
	
}
