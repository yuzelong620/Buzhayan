package com.kratos.game.herphone.dynamic.dao;

import java.util.List;
import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.kratos.game.herphone.common.CommonCost.HandlerState;
import com.kratos.game.herphone.dynamic.entity.NominateBestEntity;
@Repository
public class NominateBestDao extends BaseDao<NominateBestEntity>{

	private static final String _ID = "_id";
	private static final String HANDLERSTATE = "handlerState";
	private static final String RECOMMENDTIME = "recommendTime";
	
	/**
	 * 查询未处理推荐评论表
	 * */
	public List<NominateBestEntity> findByUndeal(int page,int count){
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria=Criteria.where(HANDLERSTATE).ne(HandlerState.hold.ordinal());
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,RECOMMENDTIME)));
		query.skip(offset).limit(limit);
		return super.find(query);
	}
	
	
	/**
	 * 改变处理状态
	 * */
	public void changeHandlerState(String discussId,HandlerState state){
		Criteria criteria=Criteria.where(_ID).is(discussId);
		Query query = new Query(criteria);
		Update update =  new Update(); 
		update.set(HANDLERSTATE,state.ordinal());
		super.updateFirst(query, update);
	}
	
	/**
	 *查询推荐评论总条数
	 * */
	public long findCount() {
		Criteria criteria=Criteria.where(HANDLERSTATE).ne(HandlerState.hold.ordinal());
		Query query = new Query(criteria); 
		return super.count(query);
	}
	

	
	/**
	 *查询搁置状态的评论
	 * */
	public List<NominateBestEntity> findByhold(int page,int count){
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria=Criteria.where(HANDLERSTATE).is(HandlerState.hold.ordinal());
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,RECOMMENDTIME)));
		query.skip(offset).limit(limit);
		return super.find(query);
		
	}
	
	/**
	 *查询搁置状态的评论总条数
	 * */
	public long findcountByhold(){
		Criteria criteria=Criteria.where(HANDLERSTATE).is(HandlerState.hold.ordinal());
		Query query = new Query(criteria); 
		return super.count(query);
	}
	
	/**
	 * 根据playerid查询信息
	 * */
	public List<NominateBestEntity> findByIds(List<Long> playerid) {
		Criteria criteria = new Criteria(_ID).in(playerid);
		Query query = new Query(criteria); 
		return find(query);
	}


	public void setRecommendUtd(String dynamicId) {
		Criteria criteria = new Criteria(_ID).in(dynamicId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set(HANDLERSTATE,HandlerState.undeal.ordinal());
		super.updateFirst(query, update);
	}
	

	
}
