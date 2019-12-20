package com.kratos.game.herphone.systemMessgae.dao;
import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.systemMessgae.entity.SystemMessgaeEntity;

@Repository
public class SystemMessgaeDao extends BaseDao<SystemMessgaeEntity>{
	private static final String CREATE_TIME = "createTime";
	private static final String PLAYERID = "playerId";
	private static final String READ_STATE ="readState";
	private static final String ANNOUNCEMENT ="announcement";
	
	public List<SystemMessgaeEntity> distinctFindList() {					
		return distinct(PLAYERID);
	}
	public void updateFromGm(long playerId,int state,int setState) {
		Criteria criteria = Criteria.where(PLAYERID).is(playerId);
		criteria.and(READ_STATE).is(state);
		Query query = new Query(criteria);
		Update update =  new Update();
		update.set(READ_STATE, setState);
		updateMulti(query, update);
	}
	public List<SystemMessgaeEntity> findByPlayerId(long playerId) {
		Criteria criteria = Criteria.where(PLAYERID).is(playerId);
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC, CREATE_TIME)));// 倒序，sortID
		return find(query);
	}
	public List<SystemMessgaeEntity> findUnReadState(long playerId,int state) {
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
	/**玩家获取系统消息 包括公共系统消息*/
	public List<SystemMessgaeEntity> findByPlayerIdLimit(int page,int count,long playerId) {
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
	public SystemMessgaeEntity findLastSystemMessgaeEntity(long playerId) {
		Criteria criteria = Criteria.where(PLAYERID).is(playerId);
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC, CREATE_TIME)));// 倒序，sortID
		return findOne(query);
	}
	/**
	 * 玩家消息状态
	 * @param toPlayerId
	 * @return 返回玩家消息状态和系统时间
	 */
	public SystemMessgaeEntity findOneUnreadReply(long playerId) {	
		Criteria criteria = new Criteria(PLAYERID).is(playerId);// 被回复者的id
		Criteria state = new Criteria(READ_STATE).is(ReadState.unread.ordinal());
		Query query = new Query(criteria).addCriteria(state);
		return super.findOne(query);
	}
}
