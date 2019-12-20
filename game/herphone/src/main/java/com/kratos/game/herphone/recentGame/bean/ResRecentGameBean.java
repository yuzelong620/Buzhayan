package com.kratos.game.herphone.recentGame.bean;

import java.util.ArrayList;
import java.util.List;

import com.kratos.game.herphone.recentGame.entity.RecentGameEntity;

import lombok.Data;

@Data
public class ResRecentGameBean {
	  String id;
	  List<Integer> list= new ArrayList<Integer>(6); //最近游戏id 
  
	public ResRecentGameBean(RecentGameEntity recentGameEntity) {
		super();
		this.id = String.valueOf(recentGameEntity.getPlayerId());
		this.list = recentGameEntity.getList();
	}
	
	public ResRecentGameBean() {
		super();
	}
  
}
