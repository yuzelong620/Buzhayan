package com.kratos.game.herphone.intimacy.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.intimacy.entity.IntimacyEntity;
@Repository
public class IntimacyDao extends BaseDao<IntimacyEntity> {

	private static final String FROM_PLAYER_ID = "fromPlayerId";
	/**
	 * 查询用户所有的亲密度信息	
	 * @param fromPlayerId
	 * @return
	 */
	public List<IntimacyEntity> findByPlayerId(long fromPlayerId,int page, int count) {
		int offset = (page - 1)*count;
		int limit = count;  
		Query query = new Query() 
				.addCriteria(Criteria.where(FROM_PLAYER_ID).is(fromPlayerId));
		query.skip(offset).limit(limit);
		return find(query); 
	}
}
