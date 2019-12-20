package com.kratos.game.herphone.systemNotice.dao;

import java.util.List;
import java.util.Set;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.systemNotice.entity.SystemNoticeEntity;

@Repository
public class SystemNoticeDao extends BaseDao<SystemNoticeEntity>{
	private static final String CREATE_TIME = "createTime";
	private static final String PLAYERID = "playerId";
	private static final String READ_STATE ="readState";
	private static final String ANNOUNCEMENT ="announcement";
	
	/**根据playerId获取消息通知*/
	public List<SystemNoticeEntity> findByPlayerIdLimit(long playerId,int count,int page) {
		int offset = (page - 1) * count;
		int limit = count;	
		Criteria left = Criteria.where(PLAYERID).is(playerId);
		Criteria right = Criteria.where(ANNOUNCEMENT).is(1);
		Criteria main=new Criteria()
				.orOperator(
						left,
						right
				);
		Query query = new Query(main);
		query.skip(offset);
		query.limit(limit);
		query.with(new Sort(new Order(Direction.DESC, CREATE_TIME)));// 倒序，sortID
		return find(query);
	}
	/**修改玩家未读消息变成已读*/
	public void updateSystemNoticeUnread(long playerId) {
		Criteria criteria = Criteria.where(PLAYERID).is(playerId);
		criteria.and(READ_STATE).is(0);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set(READ_STATE, 1);
		updateMulti(query, update);		
	}
	public List<SystemNoticeEntity> findUnReadState(long playerId,int state) {
		Criteria left = new Criteria()
				.andOperator(
						Criteria.where(PLAYERID).is(playerId),
						Criteria.where(READ_STATE).is(state)
						);
		Criteria right = Criteria.where(ANNOUNCEMENT).is(1);
		Criteria main=new Criteria()
				.orOperator(
						left,
						right
				);	
		Query query = new Query(main);
		return find(query);
	}
	public void setReadState(Long id, Set<String> systemNoticeIds) {
		// TODO Auto-generated method stub
		Query query = new Query(); 
		Criteria criteria = new Criteria("_id").in(systemNoticeIds).and("playerId").is(id);
		query.addCriteria(criteria); 
		Update update =  new Update();
		update.set("readState", ReadState.read.ordinal());
		super.updateMulti(query, update);
	}
}
