package com.kratos.game.herphone.discuss.dao;

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

import com.kratos.game.herphone.common.CommonCost.IsBest;
import com.kratos.game.herphone.common.CommonCost.IsDelete;
import com.kratos.game.herphone.common.CommonCost.IsHot;
import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.player.domain.Player;

@Repository
public class DiscussDao extends BaseDao<DiscussEntity> {
	private static final String IS_DELETE = "isDelete";
	private static final String IS_BEST = "isBest";
	private static final String REPLIE_NUM = "replieNum";
	private static final String GROUP_ID = "groupId";
	private static final String READ_STATE = "readState";
	private static final String _ID = "_id";
	private static final String IS_HOT = "isHot";
	private static final String FROM_PLAYER_ID = "fromPlayerId";
	private static final String LAST_REPLY_UPDATE_TIME = "lastReplyUpdateTime";
	private static final String TO_PLAYER_ID = "toPlayerId";
	private static final String CREATE_TIME = "createTime";
	private static final String TO_DISCUSS_ID = "toDiscussId";
	private static final String PRAISE_NUM = "praiseNum";
	private static final String DYNAMIC_ID = "dynamicId";
	private static final String DELETETIME = "deleteTime";
	private static final String TITLETIME = "titleTime";
	/**撤销删除*/
	public boolean cancelDelete(String id){
		Criteria criteria=Criteria.where(_ID).is(id);
		Query query = new Query(criteria);
		Update update=new Update();
		update.set(IS_DELETE,IsDelete.none.ordinal());
		return super.updateFirst(query, update);
	}
	/**
	 * 查询已经删除的评论列表
	 * @param page
	 * @param count
	 * @return
	 */
	public List<DiscussEntity> findDeleteDiscusses(int page,int count){
		int offset = (page - 1) * count;
		int limit = count; 
		Query query = new Query().addCriteria(Criteria.where(IS_DELETE).is(IsDelete.deleted.ordinal()));
		query.with(new Sort(new Order(Direction.DESC,DELETETIME))); 
		query.skip(offset).limit(limit);
		return find(query);
	}
	/**
	 * 设置为神评
	 * @param dynamicId
	 * @return
	 */
	public boolean setIsBest(String discussId,long titleTime) {
		Criteria criteria = new Criteria(_ID).is(discussId);
		Query query = new Query(criteria); 
		Update update=new Update();
		update.set(IS_BEST,IsBest.isBest.ordinal());
		update.set(TITLETIME, titleTime);
		return super.updateFirst(query, update);
	}
	/**
	 * 取消神评
	 */
	public boolean removeIsBest(String discussId) {
		Criteria criteria = new Criteria(_ID).is(discussId);
		Query query = new Query(criteria); 
		Update update=new Update();
		update.set(IS_BEST,IsBest.none.ordinal());
		return super.updateFirst(query, update);
	}
	/**
	 * 分组查询. 按照创建时间排序
	 * @param groupId
	 * @param page
	 * @param count
	 * @return
	 */
	public List<DiscussEntity> findByGroupId(String groupId, int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria = new Criteria(GROUP_ID).is(groupId);
		Query query = new Query(criteria)
				.addCriteria(Criteria.where(_ID).ne(groupId))
                .addCriteria(getIsNotDeleteCriteria());
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME))); 
		query.skip(offset).limit(limit);
		return find(query);
	}
	

	
	/**
     * 根据动态id,创建时间倒序，查找对系统的评论。
     * @param dynamicId 
     * @param page
     * @param count
     * @return
     */
	public List<DiscussEntity> findByDynamicId(String dynamicId,int page, int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria = new Criteria(DYNAMIC_ID)
				.is(dynamicId).and(TO_DISCUSS_ID).is("");//玩家对系统的评论
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());
		query.with(new Sort(new Order(Direction.DESC,PRAISE_NUM),new Order(Direction.DESC,CREATE_TIME)));// 点赞数 倒序排列
		query.skip(offset).limit(limit);
		return find(query);
	} 
	
	public List<DiscussEntity> findTop(String dynamicId, int count,int limitPraiseNum) {
		Criteria criteria = new Criteria(DYNAMIC_ID).is(dynamicId);
		criteria.orOperator(Criteria.where(IS_BEST).is(1),Criteria.where(PRAISE_NUM).gte(limitPraiseNum));
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		
		query.with(new Sort(new Order(Direction.DESC,IS_BEST),new Order(Direction.DESC,PRAISE_NUM))); //神评优先，然后按照评论数 排序
		query.limit(count);
		return find(query);
	}
     
	public List<DiscussEntity> findByPageAndId(String discussId, int page,int count) {
		int offset = (page - 1)*count;
		int limit = count;
		Criteria criteria = new Criteria(TO_DISCUSS_ID).is(discussId);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME))); 
		query.skip(offset).limit(limit);
		return find(query);
	}

	public List<DiscussEntity> myReplyListByPage(Long toPlayerId, int page, int count) {
		int offset = (page - 1)*count;
		int limit = count;
		Criteria criteria = new Criteria(TO_PLAYER_ID).is(toPlayerId); 
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME))); 
		query.skip(offset).limit(limit);
		return find(query);
	}
    
    /**更新回复信息*/
	public void updateReplyInfo(String discussId, long lastReplyUpdateTime) {
		Criteria criteria=Criteria.where(_ID).is(discussId).and(LAST_REPLY_UPDATE_TIME).lt(lastReplyUpdateTime);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		
		Update update =  new Update();
		update.set(LAST_REPLY_UPDATE_TIME,lastReplyUpdateTime);//最后回复时间
		update.inc(REPLIE_NUM, 1);//回复数量
		super.updateFirst(query, update);
	}

	public List<DiscussEntity> findMydiscuss(long playerId,int page,int count) {
		int offset = (page - 1)*count;
		int limit = count;
		Criteria criteria = new Criteria(FROM_PLAYER_ID).is(playerId);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		query.with(new Sort(new Order(Direction.ASC,READ_STATE),new Order(Direction.DESC,LAST_REPLY_UPDATE_TIME)));
		query.skip(offset).limit(limit);
		return find(query);
	}

	public DiscussEntity firstUnread(Long playerId){		 
		Criteria criteria = new Criteria(FROM_PLAYER_ID).is(playerId);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		
		query.with(new Sort(new Order(Direction.ASC,CREATE_TIME)));
		return super.findOne(query);
	}
	//根据玩家id获取该玩家热门评论数量
	public int hotDiscussCount(Long playerId) {
		Criteria criteria = new Criteria(FROM_PLAYER_ID).is(playerId);
		criteria.and(IS_HOT).is(IsHot.hot.ordinal());//是热评
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		
		Long count=super.count(query);
		return count.intValue();
		 
	}
	 //把 ishot设置为1
	 public boolean updateIsHotState(String hotId) {
			Criteria criteria = new Criteria(_ID).is(hotId);
			criteria.and(IS_HOT).is(IsHot.none.ordinal());
			Query query = new Query(criteria);
			query.addCriteria(getIsNotDeleteCriteria());//没有删除
			
			Update update =  new Update();
			update.set(IS_HOT,IsHot.hot.ordinal());
			return super.updateFirst(query, update);
	}
	/**
	 * 没有删除的条件
	 * @return
	 */
	private Criteria getIsNotDeleteCriteria() {
		//使用ne 规避，老数据 没有isDelete key的情况
		return Criteria.where(IS_DELETE).ne(IsDelete.deleted.ordinal());
	} 
    //把 ishot设置为1
	public void updateIsHotState(List<String> hotIds) {
		Criteria criteria = new Criteria(_ID).in(hotIds);
		criteria.and(IS_HOT).is(IsHot.none.ordinal());
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		
		Update update =  new Update();
		update.set(IS_HOT,IsHot.hot.ordinal());
		super.updateMulti(query, update);
	}
	
    /**增加点赞数1*/
	public void addLikeNum(String discussId) {
		addLikeNum(discussId, 1);	
	}
	/**
	 * 点赞数 -1
	 */
	public void deLikeNum(String discussId) {
		addLikeNum(discussId, -1);	
	}
	/**增加点赞数1*/
	public void addLikeNum(String discussId,int num) {
		Criteria Criteria = new Criteria(_ID).is(discussId);
		super.inc(new Query(Criteria),PRAISE_NUM,num);		
	}
    /**
     * 查找一个未读回复消息
     * @param playerId
     * @return
     */
	public DiscussEntity findOneUnreadReply(long playerId) {
		Criteria criteria = new Criteria(TO_PLAYER_ID).is(playerId)
				.and(READ_STATE).is(ReadState.unread.ordinal());
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		return super.findOne(query);
	}
	public long findUnreadReply(long playerId ) {
	
		Criteria criteria = new Criteria(TO_PLAYER_ID).is(playerId)
				.and(READ_STATE).is(ReadState.unread.ordinal());
		Query query = new Query(criteria);
		
		query.addCriteria(getIsNotDeleteCriteria());
		return super.count(query);
	}
    /**
     * 设置消息为已读
     * @param unreadReplyIds 
     */
	public void setReadState(List<String> unreadReplyIds) {
		Criteria Criteria = new Criteria(_ID).in(unreadReplyIds);
		Update update =  new Update();
		update.set(READ_STATE,ReadState.read.ordinal());
		super.updateMulti(new Query(Criteria), update);	
	}
	/**
	 * 根据ID删除评论,设置isDelete=1
	 * @param id
	 */
	public void removeByIdDiscuss(String id,long deleteTime) {
		Criteria criteria=Criteria.where(_ID).is(id);
		Query query = new Query(criteria);
		Update update=new Update();
		update.set(IS_DELETE,IsDelete.deleted.ordinal());
		update.set(DELETETIME,deleteTime);
		super.updateFirst(query, update) ;
	}
	
	/**
	 * 查询所有评论
	 */
	public List<DiscussEntity> findDiscussAll(int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Query query = new Query();
		query.addCriteria(getIsNotDeleteCriteria());
		query.with(new Sort(new Order(Direction.ASC,READ_STATE),new Order(Direction.DESC,LAST_REPLY_UPDATE_TIME)));
		query.skip(offset).limit(limit);
		return super.find(query);
	}
	
	public List<DiscussEntity> findDeityDiscussAll(int page, int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria=Criteria.where(IS_BEST).is(1);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());
		query.with(new Sort(new Order(Direction.DESC,TITLETIME)));
		query.skip(offset).limit(limit);
		return super.find(query);
	}
	/**
	 * 查询数据总条数
	 */
	public long findCount() {
		Query query = new Query();
		query.addCriteria(getIsNotDeleteCriteria());
		return super.count(query);
	}
	
	/**
	   * 查询删除的评论总条数
	 */
	public long findRecoverCount() {
		Criteria criteria = new Criteria(IS_DELETE).is(1);
		Query query = new Query(criteria); 
		return super.count(query);
	}
	/**
	 * 查询神评总条数
	 */
	public long getDeityCommentCount() {
		Criteria criteria=Criteria.where(IS_BEST).is(1);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());
		return super.count(query);
	}
	/**
	 * 跟据id查找
	 * @param discussIds
	 * @return
	 */
	public List<DiscussEntity> findByIds(List<String> discussIds) {
		Criteria criteria = new Criteria(_ID).in(discussIds);
		Query query = new Query(criteria); 
		return find(query);
	}
	
	/**
	 * 根据playerid查找
	 * */
	public  List<DiscussEntity> findByPlayerids(List<Long> playerid){
		Criteria criteria = new Criteria(FROM_PLAYER_ID).in(playerid);
		Query query = new Query(criteria);
		return find(query);
	}
	
	/**
	 * 根据玩家Id查询，该玩家的评论
	 * @param playerid
	 * @return
	 */
	public  List<DiscussEntity> findByPlayerids(long playerid){
		Criteria criteria = new Criteria(FROM_PLAYER_ID).is(playerid);
		Query query = new Query(criteria);
		return find(query);
	}
	public void finByToDiscussId(String id) {
		// TODO Auto-generated method stub
		Criteria criteria = new Criteria(TO_DISCUSS_ID).is(id);
		Query query = new Query(criteria);
	}
	
	/**
	 * 根据评论Id，查询所有对该评论回复的二级评论
	 * @param discussBeanIds
	 * @param page
	 * @param count
	 * @return
	 */
	public List<DiscussEntity> findReplyMyDiscuss(Set<String> discussBeanIds, int page, int count) {
		// TODO Auto-generated method stub
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria = new Criteria(TO_DISCUSS_ID).in(discussBeanIds);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		query.with(new Sort(new Order(Direction.ASC,READ_STATE),new Order(Direction.DESC,LAST_REPLY_UPDATE_TIME)));
		query.skip(offset).limit(limit);
		return find(query);
	}
	public long findReplyMyDiscussCount(Long id) {
		// TODO Auto-generated method stub
		Criteria criteria = new Criteria(TO_PLAYER_ID).is(id).and(READ_STATE).is(0);
		Query query = new Query(criteria);
		return super.count(query);
	}
	
	
	
	
}