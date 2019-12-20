package com.kratos.game.herphone.statisticalPlayer.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import com.kratos.game.herphone.statisticalPlayer.entity.StatisticalEyeshieldPlayerEntity;

@Repository
public class StatisticalEyeshieldPlayerDao extends BaseDao<StatisticalEyeshieldPlayerEntity>{
	private static final String DATE = "date";
	private static final String SORT = "sort";
	private static final String ROLEID = "roleId";
	
	
	public List<StatisticalEyeshieldPlayerEntity> findlist(int page,int count,int date) {
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
	 * 查询指定时间的护眼大队信息总数
	 */
	public long assignEyeTimeCount(int time) {
		Criteria criteria = Criteria.where(DATE).is(time);
		Query query = new Query(criteria);
		return super.count(query);
	}

	/**
	 * 指定ID查询护眼大队玩家信息变化
	 */
	public List<StatisticalEyeshieldPlayerEntity> findByRoleIdList(int page, int count, String roleId) {
		int offset = (page - 1) *count;
		int limit = count;
		Criteria criteria = Criteria.where(ROLEID).is(roleId);
		Query query = new Query(criteria);
		query.skip(offset);
		query.limit(limit);
		query.with(new Sort(new Order(Direction.ASC, SORT)));
		return find(query);
	}

	/**
	 * 指定ID查询护眼大队玩家信息变化总数
	 */
	public long getByIdAuditPlayerCount(String roleId) {
		Criteria criteria = Criteria.where(ROLEID).is(roleId);
		Query query = new Query(criteria);
		return super.count(query);
	}

	/**
	 * 根据roleId查询player信息
	 */
	public StatisticalEyeshieldPlayerEntity findByRoleIdPlayerId(String roleId) {
		Criteria criteria = Criteria.where(ROLEID).is(roleId);
		Query query = new Query(criteria);
		return findOne(query);
	}
}
