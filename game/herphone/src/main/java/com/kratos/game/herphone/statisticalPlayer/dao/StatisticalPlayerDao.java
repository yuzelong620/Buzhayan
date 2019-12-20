package com.kratos.game.herphone.statisticalPlayer.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.statisticalPlayer.entity.StatisticalPlayerEntity;

@Repository
public class StatisticalPlayerDao extends BaseDao<StatisticalPlayerEntity>{
	private static final String DATE = "date";
	private static final String SORT = "sort";
	private static final String ROLEID = "roleId";
	
	public List<StatisticalPlayerEntity> findlist(int page,int count,int date) {
		int offset = (page - 1) * count;
		int limit = count;	
		Criteria criteria = Criteria.where(DATE).is(date);
		Query query = new Query(criteria);
		query.skip(offset);
		query.limit(limit);
		query.with(new Sort(new Order(Direction.ASC, SORT)));
		return find(query);
	}

	/**
	 * 查询指定时间的护眼大队审核信息总数
    */
	public long assignTimeCount(int time) {
		Criteria criteria = Criteria.where(DATE).is(time);
		Query query = new Query(criteria);
		return super.count(query);
	}
	
	/**
	 * 根据指定的roleId查询玩家信息
	 */
	public List<StatisticalPlayerEntity> findByIdTitleAudit(String roldId,int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;	
		Criteria criteria = Criteria.where(ROLEID).is(roldId);
		Query query = new Query(criteria);
		query.skip(offset);
		query.limit(limit);
		query.with(new Sort(new Order(Direction.ASC, SORT)));
		return find(query);
	}

	/**
	 * 查询指定ID的护眼大队审核列表的数据总数
	 */
	public long findByIdTitleAuditCount(String roleId) {
		Criteria criteria = Criteria.where(ROLEID).is(roleId);
		Query query = new Query(criteria);
		return super.count(query);
	}

	/*
	 * 根据roleId查询player信息
	 */
	public StatisticalPlayerEntity findByRoleIdPlayerId(String roleId) {
		Criteria criteria = Criteria.where(ROLEID).is(roleId);
		Query query = new Query(criteria);
		return findOne(query);
	}
}
