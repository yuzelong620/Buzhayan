package com.kratos.game.herphone.score.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class ScoreRewardEntity {
	@Id
	private long playerId =0;
	private List<Integer> ltemGameId = new ArrayList<Integer>();
	
}
