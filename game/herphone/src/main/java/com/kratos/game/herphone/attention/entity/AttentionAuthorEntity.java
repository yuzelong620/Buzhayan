package com.kratos.game.herphone.attention.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document()
public class AttentionAuthorEntity {
	@Id
	private String id;//主键
	@Indexed
	private long playerId;//用户ID
	@Indexed
	private long authorId;//作者ID
	private long attentionTime;//关注时间
	
	
	public AttentionAuthorEntity(String id, long playerId, long authorId, long attentionTime) {
		super();
		this.id = id;
		this.playerId = playerId;
		this.authorId = authorId;
		this.attentionTime = attentionTime;
	}
	
	public AttentionAuthorEntity() {
		super();
	}
	
	
}
