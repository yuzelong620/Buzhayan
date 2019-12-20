package com.kratos.game.herphone.dynamic.entity;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document
public class PlayerTagsEntity {

	public PlayerTagsEntity() {
	}

	public PlayerTagsEntity(long playerId, Set<Integer> tags) {
		this.playerId = playerId;
		this.tags = tags;
	}

	@Id
	long playerId;
	Set<Integer> tags = new HashSet<>();// 获得的成就标签
}
