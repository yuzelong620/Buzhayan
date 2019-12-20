package com.kratos.game.herphone.systemMessgae.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.systemMessgae.entity.SystemMessgaeLastEntity;
@Repository
public class SystemMessageLastDao extends BaseDao<SystemMessgaeLastEntity>{
	private static final String CREATE_TIME = "createTime";
	
	public List<SystemMessgaeLastEntity> findList(int page,int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Query query = new Query();		
		query.skip(offset);
		query.limit(limit);				
		query.with(new Sort(new Order(Direction.DESC, CREATE_TIME)));// 倒序，sortID
		return find(query);
	}
	
	/**
	 * 查询历史通知总条数
	 */
	public long getInforCount() {
		Query query = new Query();
		return super.count(query);
	}
}
