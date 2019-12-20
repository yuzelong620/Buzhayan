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

import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.discuss.entity.DiscussLikeEntity;
import com.kratos.game.herphone.player.domain.Player;
@Repository
public class DiscussLikeDao extends BaseDao<DiscussLikeEntity> {

	public List<DiscussLikeEntity> findByIds(List<String> likeIds) {
		Criteria criteria = new Criteria("_id").in(likeIds);//被回复者的id
		Query query = new Query(criteria);
		return find(query);
	}
	
	public List<DiscussLikeEntity> findByToPlayerId(long toPlayerId,int page,int count){
		int offset = (page - 1) * count;
		int limit = count;
		Criteria criteria = new Criteria("toPlayerId").is(toPlayerId);
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.ASC,"isRead"),new Order(Direction.DESC,"likeTime")));// 
		query.skip(offset).limit(limit);
		return find(query);
	}
	
	public void setReadState(String id) {
		// TODO Auto-generated method stub
		Query query = new Query(); 
		Criteria criteria = new Criteria("_id").is(id);
		query.addCriteria(criteria); 
		Update update =  new Update();
		update.set("isRead", ReadState.read.ordinal());
		super.updateMulti(query, update);
	}
	
	public void setReadState(List<String> ids) {
		// TODO Auto-generated method stub
		Query query = new Query(); 
		Criteria criteria = new Criteria("_id").in(ids);
		query.addCriteria(criteria); 
		Update update =  new Update();
		update.set("isRead", ReadState.read.ordinal());
		super.updateMulti(query, update);
	}
	/**
 	* 查询对我评论点赞但未读的点赞数量
 */
	public long findLikeCount(Player player) {
		// TODO Auto-generated method stub
		Criteria criteria=new Criteria("isRead").is(0).and("toPlayerId").is(player.getId());
		Query query = new Query(criteria);
		return super.count(query);
	}

}
