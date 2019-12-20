package com.kratos.game.herphone.recentGame.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class RecentGameEntity {
	@Id
	private long playerId;
	private List<Integer> list= new ArrayList<Integer>(6); //最近游戏id 
		
}
