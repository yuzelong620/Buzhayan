package com.kratos.game.herphone.discuss.dao;

import java.util.Collection;
import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.discuss.entity.DiscussOptionEntity;
@Repository
public class DiscussOptionDao extends BaseDao< DiscussOptionEntity>{
	/**
	 * 增加 评论数，自增1
	 * @param dynamicId
	 */
	public void incDiscussNum(String dynamicId){
		Criteria criteria=Criteria.where("_id").is(dynamicId);
		Query query = new Query(criteria);
		Update update =  new Update(); 
		update.inc("discussNum",1);//评论数+1
		super.updateFirst(query, update);
	}
	
	public List<DiscussOptionEntity> findByIds(Collection<String> ids){
		Criteria criteria=Criteria.where("_id").in(ids);
		Query query = new Query(criteria);
		return super.find(query);
	}
	
	public List<DiscussOptionEntity> findCountByGameId(int gameId){
		Criteria criteria=Criteria.where("gameId").is(gameId);
		Query query = new Query(criteria);
		return super.find(query);
	}
	
	public List<DiscussOptionEntity> findAll(){
		Query query = new Query();
		return super.find(query);
	}

}
