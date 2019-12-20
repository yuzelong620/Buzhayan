
package com.kratos.game.herphone.dynamic.dao;

import java.util.Collection;
import java.util.List;

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
import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.dynamic.bean.ResBean;
import com.kratos.game.herphone.dynamic.entity.DynamicEntity;
@Deprecated
@Repository
public class DynamicDao extends BaseDao<DynamicEntity> {
	private static final String LAST_REPLY_ID = "lastReplyId";
	private static final String DELETE_TIME = "deleteTime";
	private static final String TO_DYNAMIC_ID = "toDynamicId";
	private static final String IS_DELETE = "isDelete";
	private static final String REPLIE_NUM = "replieNum";
	private static final String GROUP_ID = "groupId";
	private static final String READ_STATE = "readState";
	private static final String _ID = "_id";
	private static final String FROM_PLAYER_ID = "fromPlayerId";
	private static final String LAST_REPLY_UPDATE_TIME = "lastReplyUpdateTime";
	private static final String TO_PLAYER_ID = "toPlayerId";
	private static final String CREATE_TIME = "createTime";
	private static final String PRAISE_NUM = "praiseNum";
	private static final String TITLETIME = "titleTime";
	private static final String IS_BEST = "isBest";
	private static final String RES_LIST="resList";
	private static final String TAGS="tags";
	/**
	 * @deprecated
	 */
	public void save(DynamicEntity obj){
		super.save(obj);
	}
	/**  查询已经删除的评论列表 */
	public List<DynamicEntity> findDeleteReplys(int page,int count){
		int offset = (page - 1) * count;
		int limit = count; 
		Query query = new Query()
				.addCriteria(Criteria.where(IS_DELETE).is(IsDelete.deleted.ordinal()))
				.addCriteria(Criteria.where(TO_PLAYER_ID).ne(0));//是回复 
			 
		query.with(new Sort(new Order(Direction.DESC,DELETE_TIME))); 
		query.skip(offset).limit(limit);
		return find(query);
	}
	/** 查询 动态 */
	public List<DynamicEntity> findReplysAll(int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Query query = new Query();
		query.addCriteria(getIsNotDeleteCriteria())
		.addCriteria(Criteria.where(TO_PLAYER_ID).ne(0));//回复
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));
		query.skip(offset).limit(limit);
		return super.find(query);
	}
	/**  查询已经删除的评论列表 */
	public List<DynamicEntity> findDeleteDynamics(int page,int count){
		int offset = (page - 1) * count;
		int limit = count; 
		Query query = new Query()
				.addCriteria(Criteria.where(IS_DELETE).is(IsDelete.deleted.ordinal()))
				.addCriteria(Criteria.where(TO_PLAYER_ID).is(0));//是动态 
			 
		query.with(new Sort(new Order(Direction.DESC,DELETE_TIME))); 
		query.skip(offset).limit(limit);
		return find(query);
	}
	/** 查询 动态 */
	public List<DynamicEntity> findDynamicAll(int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Query query = new Query();
		query.addCriteria(getIsNotDeleteCriteria())
		.addCriteria(Criteria.where(TO_PLAYER_ID).is(0));//toPlayerId==0 说明是动态，不是回复信息
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));
		query.skip(offset).limit(limit);
		return super.find(query);
	}
	
	/**  查询数据总条数 */
	public long findCount() {
		Query query = new Query();
		query.addCriteria(getIsNotDeleteCriteria())
		.addCriteria(Criteria.where(TO_PLAYER_ID).is(0));//toPlayerId==0 说明是动态，不是回复信息
		return super.count(query);
	}
	
	/** 查询删除状态的条数 */
	public long deleteCount() {
		Query query = new Query()
			.addCriteria(Criteria.where(IS_DELETE).is(IsDelete.deleted.ordinal()))
			.addCriteria(Criteria.where(TO_PLAYER_ID).is(0));
		return super.count(query);
	}
	
	/**撤销删除*/
	public boolean cancelDelete(String id){
		Criteria criteria=Criteria.where(_ID).is(id);
		Query query = new Query(criteria);
		Update update=new Update();
		update.set(IS_DELETE,IsDelete.none.ordinal());
		return super.updateFirst(query, update);
	}
	
//-----------------------------GM---------分界线----------------------------------------- 
	/**
	 * 分组查询. 按照创建时间排序
	 * @param groupId
	 * @param page
	 * @param count
	 * @return
	 */
	public List<DynamicEntity> findReplyByGroupId(String groupId, int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria = new Criteria(GROUP_ID).is(groupId);
		Query query = new Query(criteria)
				.addCriteria(Criteria.where(_ID).ne(groupId))
                .addCriteria(getIsNotDeleteCriteria());
		query.with(new Sort(new Order(Direction.DESC,PRAISE_NUM),new Order(Direction.DESC,CREATE_TIME)));
		query.skip(offset).limit(limit);
		return find(query);
	}
	 
 
    /**
     * 查找回复列表
     */
	public List<DynamicEntity> findReplayList(String toDynamicId, int page,int count) {
		int offset = (page - 1)*count;
		int limit = count;
		Criteria criteria = new Criteria(TO_DYNAMIC_ID).is(toDynamicId);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		
		query.with(new Sort(new Order(Direction.DESC,PRAISE_NUM),new Order(Direction.DESC,CREATE_TIME)));
		query.skip(offset).limit(limit);
		return find(query);
	}

	/**
	 * 查找 回復我的信息。 
	 */
	public List<DynamicEntity> myReplyListByPage(long toPlayerId, int page, int count) {
		int offset = (page - 1)*count;
		int limit = count;
		Criteria criteria = new Criteria(TO_PLAYER_ID).is(toPlayerId); 
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		query.with(new Sort(new Order(Direction.DESC,READ_STATE),new Order(Direction.DESC,CREATE_TIME))); 
		query.skip(offset).limit(limit);
		return find(query);
	}
    
    /**更新回复信息*/
	public void updateReplyInfo(String dynamicId, long lastReplyUpdateTime,String lastReplyId) {
		Criteria criteria=Criteria.where(_ID).is(dynamicId)
//				.and(LAST_REPLY_UPDATE_TIME).lt(lastReplyUpdateTime)
				;
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		
		Update update =  new Update();
		update.set(LAST_REPLY_UPDATE_TIME,lastReplyUpdateTime);//最后回复时间
		update.set(LAST_REPLY_ID, lastReplyId);//设置最后的 回复id
		update.inc(REPLIE_NUM, 1);//回复数量
		super.updateFirst(query, update);
	}
    /**
     * 查找我的回复消息
     */
	public List<DynamicEntity> findMyDynamicReply(long playerId,int page,int count) {
		int offset = (page - 1)*count;
		int limit = count;
		Criteria criteria = new Criteria(FROM_PLAYER_ID).is(playerId);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		query.with(new Sort(new Order(Direction.ASC,READ_STATE),new Order(Direction.DESC,LAST_REPLY_UPDATE_TIME)));
		query.skip(offset).limit(limit);
		return find(query);
	}
    /**
     * 查找是否有未读消息
     */
	public DynamicEntity firstUnread(Long playerId){		 
		Criteria criteria = new Criteria(FROM_PLAYER_ID).is(playerId);
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		
		query.with(new Sort(new Order(Direction.ASC,CREATE_TIME)));
		return super.findOne(query);
	}
  
	
	
    /**增加点赞数1*/
	public void addLikeNum(String discussId) {
		addLikeNum(discussId, 1);	
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
	public DynamicEntity findOneUnreadReply(long playerId) {
		Criteria criteria = new Criteria(TO_PLAYER_ID).is(playerId)
				.and(READ_STATE).is(ReadState.unread.ordinal());
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		return super.findOne(query);
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
	public void setIsDelete(String id) {
		Criteria criteria=Criteria.where(_ID).is(id);
		Query query = new Query(criteria);
		Update update=new Update();
		update.set(DELETE_TIME, System.currentTimeMillis());
		update.set(IS_DELETE,IsDelete.deleted.ordinal());
		super.updateFirst(query, update) ;
	}
	/**
	 * 设置为神评
	 * @param dynamicId
	 * @return
	 */
	public boolean setIsBest(String dynamicId,long titleTime) {
		Criteria criteria = new Criteria(_ID).is(dynamicId);
		Query query = new Query(criteria); 
		Update update=new Update();
		update.set(IS_BEST,IsBest.isBest.ordinal());
		update.set(TITLETIME, titleTime);
		return super.updateFirst(query, update);
	}
	/**
	 * 取消神评
	 */
	public boolean removeIsBest(String dynamicId) {
		Criteria criteria = new Criteria(_ID).is(dynamicId);
		Query query = new Query(criteria);		
		Update update=new Update();
		update.set(IS_BEST,IsBest.none.ordinal());
		return super.updateFirst(query, update);
	}
	
	//----------------------------------------------------------------------------------------------------
	/**
	 *查找动态
	 */
	public List<DynamicEntity> findDynamics(int page, int count) {
		int offset = (page - 1)*count;
		int limit = count;  
		Query query = new Query()
				.addCriteria(Criteria.where(TO_PLAYER_ID).is(0))
		        .addCriteria(getIsNotDeleteCriteria());//没有删除
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));//根据点赞数倒叙排列
		query.skip(offset).limit(limit);
		return find(query);		
	}
	/**
	 * 根据tag动态
	 */
	public List<DynamicEntity> findDynamicsByTag(Collection<Integer> tags, int page, int count) {
		int offset = (page - 1)*count;
		int limit = count;  
		Query query = new Query()
				.addCriteria(Criteria.where(TO_PLAYER_ID).is(0))
				.addCriteria(Criteria.where("tags").in(tags))
		        .addCriteria(getIsNotDeleteCriteria());//没有删除
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));//根据点赞数倒叙排列
		query.skip(offset).limit(limit);
		return find(query); 
	}
	/**
	 * 根据id 查找动态 
	 */
	public List<DynamicEntity> findDynamics(List<Long> ids, int page, int count) {
		int offset = (page - 1)*count;
		int limit = count;  
		Query query = new Query() 
				.addCriteria(Criteria.where(TO_PLAYER_ID).in(ids))
		        .addCriteria(getIsNotDeleteCriteria());//没有删除
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));//根据点赞数倒叙排列
		query.skip(offset).limit(limit);
		return find(query); 
	}
	/**
	 * 分页查找玩家动态
	 * @param playerId
	 * @param page
	 * @param count
	 * @return
	 */
	public List<DynamicEntity> findDynamics(String id, int page, int count) {
		int offset = (page - 1)*count;
		int limit = count;  
		Query query = new Query() 
				.addCriteria(Criteria.where(TO_PLAYER_ID).is(id))
		        .addCriteria(getIsNotDeleteCriteria());//没有删除
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));//根据点赞数倒叙排列
		query.skip(offset).limit(limit);
		return find(query); 
	}
	
	/**
	 * 分页查找玩家动态
	 * @param playerId
	 * @param page
	 * @param count
	 * @return
	 */
	public List<DynamicEntity> findDynamics(long playerId, int page, int count) {
		int offset = (page - 1)*count;
		int limit = count; 
		Query query = new Query()
				.addCriteria(Criteria.where(FROM_PLAYER_ID).is(playerId))
				.addCriteria(Criteria.where(TO_PLAYER_ID).is(0))
		        .addCriteria(getIsNotDeleteCriteria());//没有删除
		 
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));
		query.skip(offset).limit(limit);
		return find(query); 
	}
	/**
	 * 没有删除的条件
	 * @return
	 */
	private Criteria getIsNotDeleteCriteria() {
		//使用ne 规避，老数据 没有isDelete key的情况
		return Criteria.where(IS_DELETE).ne(IsDelete.deleted.ordinal());
	}
	/**
	 * 查找 根据id集合
	 * @param ids
	 * @return
	 */
	public List<DynamicEntity> findByIds(List<String> ids) {
		Query query = new Query()
				.addCriteria(Criteria.where(_ID).in(ids));
		return super.find(query);
	}
	public void updateDynamic(List<Integer> tags,List<ResBean> resList, long fromPlayerId,String id) {
		Criteria criteria=Criteria.where(_ID).is(id)
//				.and(LAST_REPLY_UPDATE_TIME).lt(lastReplyUpdateTime)
				;
		Query query = new Query(criteria);
		query.addCriteria(getIsNotDeleteCriteria());//没有删除
		Update update =  new Update();
		update.set(FROM_PLAYER_ID,fromPlayerId);
		update.set(RES_LIST, resList);//更新conntent中的内容
		update.set(TAGS,tags);//回复标签值
		super.updateFirst(query, update);
	}
	public long findReplyDynamic(long toPlayerId) {
		Criteria criteria = new Criteria(TO_PLAYER_ID).is(toPlayerId)
				.and(READ_STATE).is(ReadState.unread.ordinal());
		Query query = new Query(criteria);
		
		query.addCriteria(getIsNotDeleteCriteria());
		return super.count(query);
	}
	
	/**
     * 用户通过标签查询广场内容
     */
	public List<DynamicEntity> findByTagsDynamic(int page,int count,int tags) {
		int offset = (page - 1)*count;
		int limit = count;  
		Criteria criteria=Criteria.where(TAGS).in(tags).and(IS_DELETE).is(IsDelete.none.ordinal()); //查找指定标签没有删除的内容
		Query query = new Query(criteria)
		 .addCriteria(Criteria.where(TO_PLAYER_ID).is(0));
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));
		query.skip(offset).limit(limit);
		return super.find(query);
	}
}