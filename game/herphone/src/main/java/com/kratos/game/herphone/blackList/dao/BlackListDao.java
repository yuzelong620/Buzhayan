package com.kratos.game.herphone.blackList.dao;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.blackList.entity.BlackListEntity;

@Repository
public class BlackListDao extends BaseDao<BlackListEntity>{
	private static final String ID ="_id";
	private static final String BLACK_LIST ="blackList";
	
	public boolean addBlack(long playerId,long toPlayerId) {
		Criteria criteria = new Criteria(ID).is(playerId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.addToSet(BLACK_LIST, toPlayerId);
		return updateFirst(query, update);		
	}
	public boolean removeBlack(long playerId,long toPlayerId) {
		Criteria criteria = new Criteria(ID).is(playerId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.pull(BLACK_LIST, toPlayerId);
		return updateFirst(query, update);		
	}
}
