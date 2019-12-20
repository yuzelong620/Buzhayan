package com.kratos.game.herphone.clue.entity;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document
public class PlayerClueEntity {
	@Id
	long playerId;
	Set<Integer> rewardClueIds = new HashSet<>();

	public PlayerClueEntity(long playerId, Set<Integer> rewardClueIds) {
		this.playerId = playerId;
		this.rewardClueIds = rewardClueIds;
	}

	public PlayerClueEntity() {
	}

}
