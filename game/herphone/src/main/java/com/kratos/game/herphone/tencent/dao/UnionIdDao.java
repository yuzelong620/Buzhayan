package com.kratos.game.herphone.tencent.dao;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.tencent.entity.UnionIdEntity;
@Repository
public class UnionIdDao extends BaseDao<UnionIdEntity> {
	public UnionIdEntity findByUnionId(String unionid){
		Query query=new Query(new Criteria("tencentUnionId").is(unionid));
		return super.findOne(query);
	}
	
}
