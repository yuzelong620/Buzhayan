package com.kratos.game.herphone.bag.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BagEntity {
	@Id
	private long playerId;
	private Map<Integer, Integer> bagItems = new HashMap<Integer, Integer>();//所有的物品

	
	// getter and setter 
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public Map<Integer, Integer> getBagItems() {
		return bagItems;
	}

	public void setBagItems(Map<Integer, Integer> bagItems) {
		this.bagItems = bagItems;
	}

}
