package com.kratos.game.herphone.statisticalPlayer.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.statisticalPlayer.entity.StatisticalPioneerEntity;

@Repository
public class StatisticalPioneerDao extends BaseDao<StatisticalPioneerEntity>{
	private static final String DATE = "date";
	private static final String CREAT_TIME = "creatTime";
	
	
	public List<StatisticalPioneerEntity> findlist(int page,int count,int date) {
		int offset = (page - 1) * count;
		int limit = count;	
		Criteria criteria = new Criteria(DATE).is(date);
		Query query = new Query(criteria);
		query.skip(offset);
		query.limit(limit);
		query.with(new Sort(new Order(Direction.DESC, CREAT_TIME)));
		return find(query);
	}

	/**
	 * 根据时间查询护眼先锋总数
	 */
	public long getByTimePioneerCount(int date) {
		Criteria criteria = new Criteria(DATE).is(date);
		Query query = new Query(criteria); 
		return super.count(query);
	}
}
