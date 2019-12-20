package com.kratos.game.herphone.playerOnline.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.globalgame.auto.json.GameParams_Json;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.playerOnline.entity.PlayerOnlineEntity;

@Repository
public class PlayerOnlineDao extends BaseDao<PlayerOnlineEntity> {

	private static final String ONLINETIME = "onlineTime";
	private static final String ID = "_id";
	/**
	 * 查到满足在线时间达到72小时以上玩家列表
	 */
	public List<PlayerOnlineEntity> meet72HoursPlayer(List<Long> list) {
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		Criteria Criteria = new Criteria(ID).in(list);
		Criteria.and(ONLINETIME).gte(gameParams_Json.getTitleOnline()*60*60L);
		Query query = new Query(Criteria);
		List<PlayerOnlineEntity> playerOnlineEntitys = super.find(query);
		return playerOnlineEntitys;	
	}
	
	/**
	 * 根据playerid查询信息
	 * */
	public List<PlayerOnlineEntity> findByIdOnlineTime(List<Long> playerid) {
		Criteria criteria = new Criteria(ID).in(playerid);
		Query query = new Query(criteria); 
		return find(query);
	}

}
