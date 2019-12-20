package com.kratos.game.herphone.dynamic.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import com.kratos.game.herphone.dynamic.entity.DynamicLikeEntity;
@Repository
public class DynamicLikeDao extends BaseDao<DynamicLikeEntity> {

	public List<DynamicLikeEntity> findByIds(List<String> likeIds) {
		Criteria criteria = new Criteria("_id").in(likeIds);//被回复者的id
		Query query = new Query(criteria);
		return find(query);
	}

}
