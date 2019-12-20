package com.kratos.game.herphone.rank.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.rank.entity.CluesRankEntity;

@Repository
public class CluesRankDao extends BaseDao<CluesRankEntity>{	
	private static final String _ID = "_id";
	private static final String PLAYER_ID = "playerDynamicEntity._id";
	private static final String ACHIEVEMENT_DEBRIS = "playerDynamicEntity.achievementDebris";
		
	public List<CluesRankEntity> listCluesRank(int page,int count) {	
		int offset = (page - 1)*count;
		int limit = count;
		Criteria criteria = new Criteria(ACHIEVEMENT_DEBRIS).gt(0);
		Query query = new Query(criteria); 	
		query.with(new Sort(new Order(Direction.ASC,_ID)));// 倒序，sortID
		query.skip(offset).limit(limit);			
		return find(query);		
	}
	
	public CluesRankEntity getMyselfRank(long playerId) {
		Criteria criteria = new Criteria(PLAYER_ID).is(playerId);
		Query query = new Query(criteria); 	
		return findOne(query);
	}
}
