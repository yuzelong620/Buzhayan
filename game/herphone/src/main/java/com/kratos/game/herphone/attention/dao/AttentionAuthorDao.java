package com.kratos.game.herphone.attention.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Update;

import com.kratos.game.herphone.attention.entity.AttentionAuthorEntity;
import com.kratos.game.herphone.attention.entity.AttentionEntity;

@Repository
public class AttentionAuthorDao extends BaseDao<AttentionAuthorEntity>{
	
	static final String key_PlayerId="playerId";
	
	public List<AttentionAuthorEntity> findAttentionAuthor(long playerId) {
		Criteria criteria = new Criteria(key_PlayerId).is(playerId);
		Query query = new Query(criteria);
		return find(query);
	}
  
}
