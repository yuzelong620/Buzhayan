package com.kratos.game.herphone.clue.dao;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.clue.entity.PlayerClueEntity;

@Repository
public class PlayerClueDao extends BaseDao<PlayerClueEntity>{
	private static final String ID ="_id";
	private static final String REWARD_CLUE_IDS ="rewardClueIds";
	
	public boolean addBlack(long playerId,int clueId) {
		Criteria criteria = new Criteria(ID).is(playerId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.addToSet(REWARD_CLUE_IDS, clueId);
		return updateFirst(query, update);		
	}
}
