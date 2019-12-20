package com.kratos.game.herphone.bag.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.kratos.game.herphone.bag.entity.BagEntity;
@Repository
public class BagDao extends BaseDao<BagEntity> {
	
	private static final String _ID = "_id";

	public void updateNum(long playerId, HashMap<Integer, Integer> items) {
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query = new Query().addCriteria(criteria);
		Update update = new Update();
		for (Entry<Integer, Integer> entry : items.entrySet()) {
			update.inc("bagItems." + entry.getKey(), entry.getValue());
		}
		super.updateFirst(query, update);
	}
	
}
