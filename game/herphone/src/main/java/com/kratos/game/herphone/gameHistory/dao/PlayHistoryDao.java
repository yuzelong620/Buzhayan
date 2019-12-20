package com.kratos.game.herphone.gameHistory.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.gameHistory.entity.PlayHistoryEntity;

@Repository
public class PlayHistoryDao extends BaseDao<PlayHistoryEntity> {
	
	/** 添加關注數量 */
	public void incShowNum(int gameId, int num) {
		Criteria Criteria = new Criteria("_id").is(gameId);
		super.inc(new Query(Criteria), "showNum", num);
	}
	
	public List<PlayHistoryEntity> findAll() {
		Query query = new Query();
		return  super.find(query);
	}
}
