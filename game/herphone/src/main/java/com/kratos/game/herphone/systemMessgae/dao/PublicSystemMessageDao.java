package com.kratos.game.herphone.systemMessgae.dao;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.systemMessgae.entity.PublicSystemMessageEntity;

@Repository
public class PublicSystemMessageDao extends BaseDao<PublicSystemMessageEntity>{
	private static final String ID ="_id";
	private static final String SYSTEM_MESSAGE_SET ="systemMessageSet";
	private static final String SYSTEM_NOTICE ="systemNotice";
	
	public boolean addSystemMessageSet(long playerId,String systemMessageId) {
		Criteria criteria = new Criteria(ID).is(playerId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.addToSet(SYSTEM_MESSAGE_SET, systemMessageId);
		return updateFirst(query, update);		
	}
	public boolean addSystemNotice(long playerId,String systemNoticeId) {
		Criteria criteria = new Criteria(ID).is(playerId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.addToSet(SYSTEM_NOTICE, systemNoticeId);
		return updateFirst(query, update);		
	}
}
