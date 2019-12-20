package com.kratos.game.herphone.attention.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Update;
import com.kratos.game.herphone.attention.entity.AttentionEntity;

@Repository
public class AttentionDao extends BaseDao<AttentionEntity>{
	
	static final String key_fromPlayerId="fromPlayerId";
	static final String key_toPlayerId="toPlayerId";
    /**
     * 查找我关注的粉丝
     * @param userIds 我关注的粉丝
     * @param userId 
     * @return
     */
	public List<AttentionEntity> seachAttentions(List<String> ids) {
		Query query = new Query();
		query.addCriteria(new Criteria("_id").in(ids));
		query.limit(ids.size());
		return find(query);
	}
    /**
     * 查找我关注的人
     * @param userId
     * @param page
     * @param count
     * @return
     */
	public List<AttentionEntity> seachAttentions(long userId, int page, int count) { 
		int offset = (page - 1)*count;
		int limit = count;
		Criteria criteria = new Criteria(key_fromPlayerId).is(userId);
		Query query = new Query(criteria);
		query.skip(offset).limit(limit);
		return find(query);
	}

	public List<AttentionEntity> seachFans(long userId, int page, int count) {
		int offset = (page - 1)*count;
		int limit = count;
		Criteria criteria = new Criteria(key_toPlayerId).is(userId);
		Query query = new Query(criteria);
		query.skip(offset).limit(limit);
		return find(query);
	}
	/**\
	 * 更新 from信息
	 * @param fromPlayerId
	 * @param fromPlayerNick
	 * @param fromPlayerAvatarUrl
	 * @param fromPlayerSignature
	 */
	public  void  updateFromPlayer(long fromPlayerId,String fromPlayerNick,String fromPlayerAvatarUrl,String fromPlayerSignature ,
			int fromAchievementTags ,int fromAvatarFrame){
        Criteria criteria = new Criteria(key_fromPlayerId).is(fromPlayerId);
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("fromPlayerNick", fromPlayerNick);
		update.set("fromPlayerAvatarUrl", fromPlayerAvatarUrl);
		update.set("fromPlayerSignature", fromPlayerSignature); 
		update.set("fromAchievementTags", fromAchievementTags);
		update.set("fromAvatarFrame", fromAvatarFrame);
		super.updateMulti(query, update);
	}
	/**
	 * 更新to 信息
	 * @param toPlayerId
	 * @param toPlayerNick
	 * @param toPlayerAvatarUrl
	 * @param toPlayerSignature
	 */
	public  void  updateToPlayer(long toPlayerId,String toPlayerNick,String toPlayerAvatarUrl,String toPlayerSignature,
			int toAchievementTags ,int toAvatarFrame){
        Criteria criteria = new Criteria(key_toPlayerId).is(toPlayerId);
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("toPlayerNick", toPlayerNick);
		update.set("toPlayerAvatarUrl", toPlayerAvatarUrl);
		update.set("toPlayerSignature", toPlayerSignature);
		update.set("toAchievementTags", toAchievementTags);
		update.set("toAvatarFrame", toAvatarFrame);
		super.updateMulti(query, update);
	}
	 
}
