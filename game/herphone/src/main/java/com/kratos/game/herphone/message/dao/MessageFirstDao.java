package com.kratos.game.herphone.message.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.message.entity.MessageFirstEntity;
@Repository
public class MessageFirstDao extends BaseDao<MessageFirstEntity>{

	public  List<MessageFirstEntity> find(long playerId,int page,int count){
		int offset = (page - 1) * count;
		int limit = count; 
		Query query = new Query()
				.addCriteria(Criteria.where("playerIds").in(playerId));
		query.with(new Sort(new Order(Direction.DESC,"lastUpdateTime"))); 
		query.skip(offset).limit(limit);
		return find(query);
	}

	public void updateInfo(String id, String messageId, long lastUpdateTime) {
		Query query = new Query()
				.addCriteria(Criteria.where("_id").is(id));
		Update update=new Update();
		update.set("messageId", messageId);
		update.set("lastUpdateTime", lastUpdateTime);
		super.updateFirst(query, update);
	}

	public void setLimitTime(String id, long playerId, long currentTimeMillis) {
		Query query = new Query()
				.addCriteria(Criteria.where("_id").is(id));
		Update update=new Update();
		update.set("limitTime."+playerId, currentTimeMillis);
		super.updateFirst(query, update);
	}

}
