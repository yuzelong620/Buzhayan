package com.kratos.game.herphone.message.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageState;
import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.message.entity.MessageEntity;
@Repository
public class MessageDao extends BaseDao<MessageEntity>{

	private static final String _ID = "_id";
	private static final String CREATE_TIME = "createTime";
	private static final String FROM_PLAYER_ID = "fromPlayerId";
	private static final String STATE = "state";
	private static final String TO_PLAYER_ID = "toPlayerId"; 
 
	/**
	 * 更新 from信息
	 * @param fromPlayerId
	 * @param fromNickName
	 * @param fromAvatarUrl
	 * @param fromPlayerSignature
	 */
	public  void  updateFromPlayer(long fromPlayerId,String fromNickName,String fromAvatarUrl){
        Criteria criteria = new Criteria( FROM_PLAYER_ID).is(fromPlayerId);
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("fromNickName", fromNickName);
		update.set("fromAvatarUrl", fromAvatarUrl);
		super.updateMulti(query, update);
	}
	public void  updateToPlayer(long toPlayerId,String toNickName,String toAvatarUrl){
		Criteria criteria = new Criteria(TO_PLAYER_ID).is(toPlayerId);
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("toNickName", toNickName);
		update.set("toAvatarUrl", toAvatarUrl); 
		super.updateMulti(query, update);
	}

	public List<MessageEntity> findUnreadMessageInfo(long playerId, int page, int count){
		int offset = (page - 1)*count;
		int limit = count;
		Query query = new Query(); 

		Criteria  left= Criteria.where(FROM_PLAYER_ID).is(playerId);
		Criteria  right=Criteria.where(TO_PLAYER_ID).is(playerId);
		Criteria main=new Criteria()
		.orOperator( left, right
		);
		query.addCriteria(main);
		query.with(new Sort(new Order(Direction.ASC,STATE),new Order(Direction.DESC,CREATE_TIME)));// 倒序，sortID
		query.skip(offset).limit(limit);
		List<MessageEntity> List= find(query);
		return List;
	}
    
	public List<MessageEntity> findMessage(long fromPlayerId,long toPlayerId, int page, int count){		
		int offset = (page - 1)*count;
		int limit = count;
		Query query = new Query(); 
		
		Criteria  left=new Criteria()//条件1
		.andOperator(
				Criteria.where(FROM_PLAYER_ID).is(fromPlayerId),
				Criteria.where(TO_PLAYER_ID).is(toPlayerId)
		);
		Criteria right=new Criteria()//条件2
				.andOperator(
						Criteria.where(TO_PLAYER_ID).is(fromPlayerId),
						Criteria.where(FROM_PLAYER_ID).is(toPlayerId)
				);
		
		Criteria main=new Criteria()
		.orOperator(
				left,
				right
		);
		
		//相当于  mysql  条件1 and 条件2
		query.addCriteria(main);
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));// 倒序，sortID
		query.skip(offset).limit(limit);
		return find(query); 
	}

	public void setReadState(Set<String> messageIds) {
		Query query = new Query(); 
		query.addCriteria(new Criteria(_ID).in(messageIds)); 
		Update update =  new Update();
		update.set(STATE, ReadState.read.ordinal());
		super.updateMulti(query, update);
	}
	
	public List<MessageEntity> setReadState(long playerId) {
		Query query = new Query(); 
		query.addCriteria(new Criteria(FROM_PLAYER_ID).is(playerId).and(STATE).is(ReadState.unread.ordinal())); 
		return find(query);		
	}
	/**
	 * 查找消息根据id
	 * @param messageIds
	 * @return
	 */
	public List<MessageEntity> findByIds(Collection<String> messageIds) {
		Query query = new Query(); 
		query.addCriteria(new Criteria(_ID).in(messageIds)); 
	    return super.find(query);
	}
    
	/**
	 * 玩家消息状态
	 * @param toPlayerId
	 * @return 返回玩家消息状态和系统时间
	 */
	public MessageEntity findOneUnreadReply(Long toPlayerId) {
		Criteria criteria = new Criteria(TO_PLAYER_ID).is(toPlayerId);// 被回复者的id
		Criteria state = new Criteria(STATE).is(ReadState.unread.ordinal());
		Query query = new Query(criteria).addCriteria(state);
		return super.findOne(query);
	}
	/**
	 * 查找我的未读消息
	 * @param myPlayerId
	 * @param fromPlayerId
	 * @return
	 */
	public List<MessageEntity> findToMyUnreadMessages(long myPlayerId, List<Long> fromPlayerIds) {
		Criteria criteria = new Criteria( FROM_PLAYER_ID).in(fromPlayerIds);
		Query query = new Query(criteria)
				.addCriteria(Criteria.where(TO_PLAYER_ID).is(myPlayerId))
				.addCriteria(Criteria.where(STATE).is(ReadState.unread.ordinal()));
		query.limit(300);
		return super.find(query);
	}
	
}
