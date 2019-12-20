package com.kratos.game.herphone.intimacy.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 亲密度
 *
 */
@Data
@Document
public class IntimacyEntity {

	public IntimacyEntity() {

	}

	public IntimacyEntity(String id, long fromPlayerId, long toPlayerId, int intimacyValue) {
		this.id = id;
		this.fromPlayerId = fromPlayerId;
		this.toPlayerId = toPlayerId;
		this.intimacyValue = intimacyValue;
	}

	@Id
	private String id;// fromPlayerId_toPlayerId
	@Indexed
	private long fromPlayerId;
	@Indexed
	private long toPlayerId;
	private int intimacyValue;// 亲密度值 
	
	

}
