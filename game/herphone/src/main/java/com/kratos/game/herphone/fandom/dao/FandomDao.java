package com.kratos.game.herphone.fandom.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.common.CommonCost.IsDelete;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.fandom.entity.FandomEntity;
@Deprecated
@Repository
public class FandomDao extends BaseDao<FandomEntity> {
	private static final String _ID = "_id";
	private static final String GAME_ID = "gameId";
	private static final String CREATE_TIME = "createTime";
	private static final String IS_DELETE = "isDelete";

	/**
	 * 没有删除的条件
	 * @return
	 */
	private Criteria getIsNotDeleteCriteria() {
		//使用ne 规避，老数据 没有isDelete key的情况
		return Criteria.where(IS_DELETE).ne(IsDelete.deleted.ordinal());
	}
	/**  查询删除列表 */
	public List<FandomEntity> findDeleteList(int page,int count){
		int offset = (page - 1) * count;
		int limit = count; 
		Query query = new Query()
				.addCriteria(Criteria.where(IS_DELETE).is(IsDelete.deleted.ordinal()));
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME))); 
		query.skip(offset).limit(limit);
		return find(query);
	}
	/** 查询 列表 */
	public List<FandomEntity> findList(int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Query query = new Query();
		query.addCriteria(getIsNotDeleteCriteria());
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));
		query.skip(offset).limit(limit);
		return super.find(query);
	}
	//	.addCriteria(Criteria.where(GAME_ID).is(gameId))
	/**
	 * 跟据id查找
	 * @param discussIds
	 * @return
	 */
	public List<FandomEntity> findByIds(List<String> discussIds) {
		Criteria criteria = new Criteria(_ID).in(discussIds);
		Query query = new Query(criteria); 
		return find(query);
	} 
	
	/**  查询数据总条数 */
	public long findCount() {
		Query query = new Query();
		query.addCriteria(getIsNotDeleteCriteria());
		return super.count(query);
	}
	
	/** 查询删除状态的条数 */
	public long deleteCount() {
		Criteria criteria = new Criteria(IS_DELETE).is(1);
		Query query = new Query(criteria); 
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
	
	/**删除
	 * @param id
	 */
	public boolean delete(String id){
		Criteria criteria=Criteria.where(_ID).is(id);
		Query query = new Query(criteria);
		Update update=new Update();
		update.set(IS_DELETE,IsDelete.deleted.ordinal());
		return super.updateFirst(query, update);
	}
	
	
	///---------------------------------------GM分界------------------------------------
	
	/** 查询 列表 */
	public List<FandomEntity> findList(int gameId,int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Query query = new Query();
		query.addCriteria(getIsNotDeleteCriteria())
		     .addCriteria(Criteria.where(GAME_ID).is(gameId));
		query.with(new Sort(new Order(Direction.DESC,CREATE_TIME)));
		query.skip(offset).limit(limit);
		List<FandomEntity> list= super.find(query);
		return list;
	} 
	private static final String PRAISE_NUM = "praiseNum";
	
	/**增加点赞数1*/
	public void addLikeNum(String discussId,int num) {
		Criteria Criteria = new Criteria(_ID).is(discussId);
		super.inc(new Query(Criteria),PRAISE_NUM,num);		
	}
	
}
