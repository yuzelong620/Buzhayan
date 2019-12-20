package com.kratos.game.herphone.achievement.dao;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.achievement.entity.AchievementEntity;

@Repository
public class AchievementDao extends BaseDao<AchievementEntity>{
	private static final String ACHIEVEMENT_IDS ="achievementIds";
	private static final String BADGE_IDS ="badgeIds";
	private static final String TAGS ="tags";
	private static final String ID ="_id";
	
	public boolean updateAchievementIds(long playerId,int achievementId) {
		Criteria criteria = new Criteria(ID).is(playerId);	
		Query query = new Query(criteria);
		Update update = new Update();
		update.addToSet(ACHIEVEMENT_IDS, achievementId);
		return updateFirst(query, update);
		
	}
	public boolean updateBadgeIds(long playerId,int badgeId) {
		Criteria criteria = new Criteria(ID).is(playerId);	
		Query query = new Query(criteria);
		Update update = new Update();
		update.addToSet(BADGE_IDS, badgeId);
		return updateFirst(query, update);
		
	}
	public boolean updateTags(long playerId,int tagId) {
		Criteria criteria = new Criteria(ID).is(playerId);	
		Query query = new Query(criteria);
		Update update = new Update();
		update.addToSet(TAGS, tagId);
		return updateFirst(query, update);
		
	}
}
