package com.kratos.game.herphone.badge.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.badge.entity.BadgeEntity;

@Repository
public class BadgeDao extends BaseDao<BadgeEntity> {
	private static String ID = "_id";
	private static String UNLOCK_Num = "unlockNum";
	
	/**增加解锁人数*/
	public void incUnlockNum(int id,int num) {
		Criteria criteria = new Criteria(ID).is(id);	
		super.inc(new Query(criteria), UNLOCK_Num, num);
	}
	public List<BadgeEntity> listBadgeEntity(List<Integer> list) {
		Criteria criteria = new Criteria(ID).in(list);
		Query query = new Query(criteria);
		return find(query);
	}
}
