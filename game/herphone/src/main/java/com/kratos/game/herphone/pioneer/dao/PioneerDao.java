package com.kratos.game.herphone.pioneer.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.pioneer.entity.PioneerEntity;
@Repository
public class PioneerDao extends BaseDao<PioneerEntity>{
	private static final String ALLOCATION_NUM ="allocationNum";
	private static final String ID ="_id";
	private static final String DEAL_NUM ="dealNum";
	private static final String SUCCESS_NUM ="successNum";
	private static final String CREAT_TIME ="creatTime";
	
	/**取分配举报信息最少的三个玩家*/
	public List<PioneerEntity> listTopThreePlayer() {
		int offset = 0;
		int limit = 3;
		Query query = new Query();
		query.with(new Sort(new Order(Direction.ASC,ALLOCATION_NUM)));
		query.skip(offset).limit(limit);
		return find(query);
	}
	/**分配次数加1*/
	public void incAllocationNum(long playerId,int num) {
		Criteria Criteria = new Criteria(ID).is(playerId);
		super.inc(new Query(Criteria), ALLOCATION_NUM, num);
	}
	/**处理次数加1*/
	public void incDealNum(long playerId,int num) {
		Criteria Criteria = new Criteria(ID).is(playerId);
		super.inc(new Query(Criteria), DEAL_NUM, num);
	}
	/**成功次数加1*/
	public void incSuccessNum(long playerId,int num) {
		Criteria Criteria = new Criteria(ID).is(playerId);
		super.inc(new Query(Criteria), SUCCESS_NUM, num);
	}
	/**获取护眼先锋玩家列表*/
	public List<PioneerEntity> listPioneer(int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Query query = new Query();
		query.with(new Sort(new Order(Direction.DESC,CREAT_TIME)));
		query.skip(offset).limit(limit);
		return find(query);
	}
	
	/**
	 * 查询护眼先锋总人数
	 */
	public long pioneerCount() {
		Query query = new Query();
		return super.count(query);
	}
}
