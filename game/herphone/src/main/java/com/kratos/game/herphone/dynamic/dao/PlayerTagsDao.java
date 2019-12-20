package com.kratos.game.herphone.dynamic.dao;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.dynamic.entity.PlayerTagsEntity;

@Repository
public class PlayerTagsDao extends BaseDao<PlayerTagsEntity>{

	public boolean addToSet(long playerId, int tagId) {
		Criteria cri=new Criteria("_id").is(playerId);
		Query query=new Query(cri);
		Update update=new Update();
		update.addToSet("tags",tagId);		
		return super.updateFirst(query, update);
	}
 
}
