package com.kratos.game.herphone.illegal.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.illegal.entity.IllegalRecordEntity;

@Repository
public class IllegalRecordDao extends BaseDao<IllegalRecordEntity>{
	
	private static final String CREATE_TIME = "create_time";
	private static final String PLAYERID = "playerId";

	/**
	 * 根据ID获取指定玩家违规记录
	 */
	public List<IllegalRecordEntity> getIllegalRecord(long playerId, int page, int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria Criteria = new Criteria(PLAYERID).is(playerId);
		Query query = new Query(Criteria);
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));
		query.skip(offset).limit(limit);
		return find(query);
	}

	/**
	 * 查询指定玩家违规记录总数
	 */
	public long getIllegalRecordCount(long playerId) {
		Criteria Criteria = new Criteria(PLAYERID).is(playerId);
		Query query = new Query(Criteria);
		return super.count(query);
	}

}
