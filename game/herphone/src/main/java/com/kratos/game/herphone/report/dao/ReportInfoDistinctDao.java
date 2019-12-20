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

import com.kratos.game.herphone.report.entity.ReportInfoDistinctEntity;

@Repository
public class ReportInfoDistinctDao extends BaseDao<ReportInfoDistinctEntity>{
	private static final String PUSH_PLAYERS ="pushPlayers";
	private static final String TYPE ="type";
	private static final String REPORT_TIME ="reportTime";
	private static final String ID ="_id";
	private static final String DEAL_PLAYERS ="dealPlayers";
	private static final String VERSION ="version";
	private static final String DEAL_OPINION ="dealOpinion";
	
	
	public List<ReportInfoDistinctEntity> listReportInfoDistinctByType(long playerId,int count,int page,int type,int dealOpinion) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria = new Criteria(TYPE).is(type);
		criteria.and(PUSH_PLAYERS).is(playerId);
		criteria.and(DEAL_OPINION).is(dealOpinion);
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,REPORT_TIME)));
		query.skip(offset).limit(limit); 
		return find(query);
	}
	/**忽略举报信息*/
	public boolean ignoreReportInfoDistinct(long playerId,String id,int version) {
		Criteria criteria = new Criteria(ID).is(id);
		criteria.and(VERSION).is(version);
		Query query = new Query(criteria);
		Update update = new Update();
		update.pull(PUSH_PLAYERS, playerId);
		update.inc(VERSION, 1);
		return updateFirst(query, update);
		
	}
	/**向后台推送举报信息*/
	public boolean pushReportInfoDistinct(long playerId,String id,int version,int dealOpinion) {
		Criteria criteria = new Criteria(ID).is(id);
		criteria.and(VERSION).is(version);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set(DEAL_OPINION, dealOpinion);
		update.addToSet(DEAL_PLAYERS,playerId);
		update.pull(PUSH_PLAYERS, playerId);
		update.inc(VERSION, 1);
		return updateFirst(query, update);		
	}
	/**向后台显示护眼先锋推送的举报信息*/
	public List<ReportInfoDistinctEntity> listReportInfo(int page,int count,int dealOpinion) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria = new Criteria(DEAL_OPINION).is(dealOpinion);
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,REPORT_TIME)));
		query.skip(offset).limit(limit); 
		return find(query);		
	}
	
	/**
	 * 查询护眼先锋举报总数
	 */
	public long pioneerReportCount() {
		Criteria criteria = new Criteria(DEAL_OPINION).is(1);
		Query query = new Query(criteria);
		return super.count(query);
	}
}
