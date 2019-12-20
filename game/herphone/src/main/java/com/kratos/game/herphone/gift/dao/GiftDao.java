package com.kratos.game.herphone.gift.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.gift.entity.GiftEntity;

@Repository
public class GiftDao extends BaseDao<GiftEntity>{
	
	private static final String GOODFEELING = "goodFeeling";
	private static final String AUTHORID = "authorId";
	private static final String _ID = "_id";

	/**
	 * 改变指定玩家对指定作者的好感度总数
	 */
	public void updateGoodFeeling(String id,long goodFeeling) {
		Criteria criteria = new Criteria(_ID).is(id);
		Query query=new Query().addCriteria(criteria);
		Update update = new Update();
		update.set(GOODFEELING,goodFeeling);
		super.updateInsert(query, update);
	}
	
	/**
	 * 查询指定作者好感度排行榜
	 */
	public List<GiftEntity> findBySendPlayerId(long authorId) {
		int page = 0;
		int limit = 50;
		Criteria criteria = new Criteria(AUTHORID).is(authorId);
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,GOODFEELING)));
		query.skip(page).limit(limit);
		return find(query);
	}

}
