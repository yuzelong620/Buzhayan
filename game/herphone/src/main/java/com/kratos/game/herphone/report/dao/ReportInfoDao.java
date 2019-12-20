package com.kratos.game.herphone.report.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.report.entity.ReportInfoEntity;

@Repository
public class ReportInfoDao extends BaseDao<ReportInfoEntity>{
	
	private static final String DYNAMIC_ID = "reportInfoDynamicId";
	private static final String TIME_STAMP = "timeStamp";
	private static final String IS_TITLE = "isTitle";
	private static final String STATE = "state";
	private static final String PLAYER_ID ="fromPlayerId";
	private static final String DISCUSSID ="discussId";
	private static final String TYPE ="type";
			
	public ReportInfoEntity findInfoPlayerByDynamicId(String dynamicId) {
		Criteria criteria = new Criteria(DYNAMIC_ID).is(dynamicId);
		Query query = new Query(criteria);
		return findOne(query);
	}
	public void updateByPlayerId(long playerId) {	
		Criteria criteria = Criteria.where(PLAYER_ID).is(playerId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set(IS_TITLE, 0);
		updateMulti(query, update);
	} 
	
	public List<ReportInfoEntity> findLimitList(int page, int count,int state) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria = Criteria.where(STATE).is(state).and(IS_TITLE).is(0);
		Query query = new Query(criteria);
		query.skip(offset);
		query.limit(limit);
		query.with(new Sort(new Order(Direction.DESC, TIME_STAMP),new Order(Direction.DESC,IS_TITLE)));// 倒序，sortID
		return find(query);
	}
	
	/**
	 * 查询举报评论总条数
	 */
	public long getReportDiscussCount() {
		Criteria criteria = Criteria.where(STATE).is(0);
		Query query = new Query(criteria); 
		return super.count(query);
	}
	/**
	 * 护眼大队举报消息
	 */
	public List<ReportInfoEntity> findTitleList(int page, int count, int state) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria = Criteria.where(STATE).is(state).and(IS_TITLE).is(1);
		Query query = new Query(criteria);
		query.skip(offset);
		query.limit(limit);
		query.with(new Sort(new Order(Direction.DESC, TIME_STAMP),new Order(Direction.DESC,IS_TITLE)));// 倒序，sortID
		return find(query);
	}
	/**根据评论id和类型删除所有相关举报信息*/
	public void removeReportInfoByDiscussId(String discussId,int type) {
		Criteria criteria = new Criteria(DISCUSSID).is(discussId);
		criteria.and(TYPE).is(type);
		Query query = new Query(criteria);
		remove(query);
	}
}
