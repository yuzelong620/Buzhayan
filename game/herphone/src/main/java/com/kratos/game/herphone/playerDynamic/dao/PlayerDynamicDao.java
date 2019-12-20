package com.kratos.game.herphone.playerDynamic.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.globalgame.auto.json.GameParams_Json;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

@Repository
public class PlayerDynamicDao extends BaseDao<PlayerDynamicEntity> {
	private static final String CLUE = "clue";
	private static final String REPLY_DYNAMIC_NUM = "replyDynamicNum";
	private static final String SEND_DYNAMIC_NUM = "sendDynamicNum";
	private static final String SEND_DISCUSS_NUM = "sendDiscussNum";
	private static final String TO_LIKE_NUM = "toLikeNum";
	private static final String _ID = "_id";
	private static final String ATTENTION_COUNT = "attentionCount";
	private static final String FANS_COUNT = "fansCount";
	private static final String ITEMTITLE = "itemTitle";
	private static final String SETTITLETIME = "setTitleTime";
	private static final String RECOMMEND_BEST_NUM ="recommendBestNum";
	private static final String ROLE_ID ="roleId";
	private static final String ACHIEVEMENT_DEBRIS ="achievementDebris";
	private static final String ACHIEVEMENT_TAGS ="achievementTags";
	private static final String AVATAR_FRAME ="avatarFrame";
	private static final String NICK_NAME = "nickName";
	private static final String HEAD_IMG_URL = "headImgUrl";
	private static final String SEX = "sex";
	private static final String EXP = "exp";
	private static final String CURRENCY = "currency";
	private static final String LEVEL = "level";
	private static final String GOODFEELINGNUM = "goodFeelingNum";
	private static final String SCENARIONUM = "scenarioNum";
	
	@Deprecated
	/**保存，除了load 和 保存json机器人，不要使用这个方法。 修改里面的值使用 更新部分值的方法，以避免影响覆盖原子操作的*/
	public void save(PlayerDynamicEntity entity){
		super.save(entity);
	}
	/**
	 * 更新多個貨幣值
	 * @param playerId
	 * @param map
	 */
	public void updateMoney(long playerId,Map<String,Integer> map){
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query=new Query().addCriteria(criteria);
		Update update=new Update();
		for(Entry<String, Integer> entry:map.entrySet()){
		    update.inc(entry.getKey(), entry.getValue());
		}
		super.updateFirst(query, update);
	}
	
	/**改变眨眼星币*/
	public void updateCurrency(long playerId,int currency) {
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query=new Query().addCriteria(criteria);
		inc(query, CURRENCY, currency);	
	}
	/**升级并扣除经验*/
	public void upgrade(long playerId,int exp,int level) {
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query=new Query().addCriteria(criteria);
		Update update=new Update();
		update.set(EXP, exp);
		update.set(LEVEL, level);
		updateFirst(query, update);
	}
	/**增加经验*/
	public void addExp(long playerId,int exp) {
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query=new Query().addCriteria(criteria);
		inc(query, EXP, exp);	
	}
	/**重新计算成就和线索值*/
	public void recalculate(long playerId,int achievementDebris,int clue) {
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query=new Query().addCriteria(criteria);
		Update update=new Update().set(ACHIEVEMENT_DEBRIS, achievementDebris).set(CLUE, clue);
		super.updateFirst(query, update);
	}
	/**
	 * 更新成就碎片数
	 * @param playerId
	 * @param achievementDebris
	 * @return
	 */
	public boolean setAchievementDebris(long playerId,int achievementDebris){
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query=new Query().addCriteria(criteria);
		Update update=new Update().set(ACHIEVEMENT_DEBRIS, achievementDebris);
		return super.updateFirst(query, update);
	}
	/**更新线索值*/
	public boolean setClue(long playerId,int clue){
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query=new Query().addCriteria(criteria); 
		Update update=new Update().set(CLUE, clue);
		return super.updateFirst(query, update);
	}
	/**根据clue查询前500名*/
	public List<PlayerDynamicEntity> getPlayerDynamicByClue(){		
		int limit = 500;
		Criteria criteria = new Criteria(CLUE).gt(0);
		Query query=new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,CLUE)));
		query.limit(limit);
		return find(query);		
	}
	/**根据clue查询前500名*/
	public List<PlayerDynamicEntity> getPlayerDynamicByAchievementDebris(){		
		int limit = 500;
		Criteria criteria = new Criteria(ACHIEVEMENT_DEBRIS).gt(0);
		Query query=new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,ACHIEVEMENT_DEBRIS)));
		query.limit(limit);
		return find(query);		
	}
	/**同步数据*/
	public void synchdata(Player player) {
		Criteria Criteria = new Criteria(_ID).in(player);
		Query query = new Query(Criteria);
		Update update = new Update();
		update.set(HEAD_IMG_URL, player.getAvatarUrl());
		update.set(NICK_NAME, player.decodeName());
		update.set(SEX, player.getGender());
		updateFirst(query, update);
	}
	/** 根据ID查询*/
	public List<PlayerDynamicEntity> findByIds(Collection<Long> idlist){
		Criteria Criteria = new Criteria(_ID).in(idlist);
		Query query = new Query(Criteria);
		List<PlayerDynamicEntity> list = super.find(query);
		return list;
	}

	/**根据roleId获取玩家*/
	public PlayerDynamicEntity getPlayerDynamicEntityByRoleId(String roleId){
		Criteria Criteria = new Criteria(ROLE_ID).is(roleId);
		Query query = new Query(Criteria);
		return findOne(query);
	}
	/** 增加给别人点赞的数量 */
	public void addToLikeNum(long toPlayerId, int num) {
		Criteria Criteria = new Criteria(_ID).is(toPlayerId);
		super.inc(new Query(Criteria), TO_LIKE_NUM, num);
	}
	/** 增加给送神评数量 */
	public void recommendBestNum(long toPlayerId, int num) {
		Criteria Criteria = new Criteria(_ID).is(toPlayerId);
		super.inc(new Query(Criteria), RECOMMEND_BEST_NUM, num);
	}
	/** 增加评论數量 */
	public void addSendDiscussNum(long toPlayerId, int num) {
		Criteria Criteria = new Criteria(_ID).is(toPlayerId);
		super.inc(new Query(Criteria), SEND_DISCUSS_NUM, num);
	}
	/** 添加關注數量 */
	public void incAttentionNum(long toPlayerId, int num) {
		Criteria Criteria = new Criteria(_ID).is(toPlayerId);
		super.inc(new Query(Criteria), ATTENTION_COUNT, num);
	}

	/** 添加粉丝數量 */
	public void incFansNum(long toPlayerId, int num) {
		Criteria Criteria = new Criteria(_ID).is(toPlayerId);
		super.inc(new Query(Criteria), FANS_COUNT, num);
	}
	//更新玩家的个人资料
	public void updatePlayerDms(PlayerDynamicEntity playerDynamicEntity) {
		Criteria Criteria = new Criteria(_ID).is(playerDynamicEntity.getPlayerId());
		Query query = new Query(Criteria);
		Update update =  new Update();
		update.set("nickName", playerDynamicEntity.getNickName());
		update.set("headImgUrl", playerDynamicEntity.getHeadImgUrl());
		update.set("sex", playerDynamicEntity.getSex());
		update.set("signature", playerDynamicEntity.getSignature());
		update.set("city", playerDynamicEntity.getCity());
		super.updateFirst(query, update);
	}
    /**
     * 增加 热评数量1
     * @param playerId
     */
	public void incHotDiscussCount(long playerId) {
		Criteria Criteria = new Criteria(_ID).is(playerId);
		Query query = new Query(Criteria);
		super.inc(query,"hotDiscussCount",1);
	}
	//获取粉丝数量达到80人的玩家列表
	public List<PlayerDynamicEntity> getFansCoutGte(List<Long> playerIds) {
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		Criteria Criteria = new Criteria(_ID).in(playerIds);
		Criteria.and(FANS_COUNT).gte(gameParams_Json.getTitleFansCount());
		Query query = new Query(Criteria);
		List<PlayerDynamicEntity> list = super.find(query);
		return list;
	}
	//获取玩家列表称号
	public List<PlayerDynamicEntity> getItemTitle(List<Long> list){
		Criteria Criteria = new Criteria(_ID).in(list);
		Query query = new Query(Criteria);
		List<PlayerDynamicEntity> playerDynamicEntities = find(query);
		return playerDynamicEntities;
	}
	/**
	 * 设置玩家成护眼大队
	 */
	public void setItemTitle(long toPlayerId,List<Integer> titleList,long time) {
		Criteria Criteria = new Criteria(_ID).is(toPlayerId);
		Query query = new Query(Criteria);
		Update update =  new Update();
		update.set(ITEMTITLE, titleList);
		update.set(SETTITLETIME, time);
		updateFirst(query, update);
	}
	/**
	 * 设置玩家成护眼先锋
	 */
	public void addToSetItemTitle(long toPlayerId,int TitleId) {
		Criteria Criteria = new Criteria(_ID).is(toPlayerId);
		Query query = new Query(Criteria);
		Update update =  new Update();
		update.addToSet(ITEMTITLE, TitleId);
		updateFirst(query, update);
	}
	/**
	 * 查询护眼大队玩家 
	 */
	public List<PlayerDynamicEntity> getItemTitlePlayer(int page,int count,int titleId){
		int offset = (page - 1) * count;
		int limit = count;
		Criteria Criteria = new Criteria(ITEMTITLE).is(titleId);
		Query query = new Query(Criteria);
		query.with(new Sort(new Order(Direction.DESC,SETTITLETIME)));
		query.skip(offset).limit(limit);
		return find(query);
	}
	/**
	 * 查询护眼先锋玩家 
	 */
	public List<PlayerDynamicEntity> getItemTitlePlayerByPioneer(int titleId){		
		Criteria Criteria = new Criteria(ITEMTITLE).is(titleId);
		Query query = new Query(Criteria);		
		return find(query);
	}
	/**
	 * 查询护眼大队玩家总人数
	 * @param protectEyesTitleId 
	 */
	public long getItemTitleCount(Integer protectEyesTitleId) {
		Criteria criteria = new Criteria(ITEMTITLE).is(protectEyesTitleId);
		Query query = new Query(criteria); 
		return super.count(query);
	}
	
	/**
	 * 查询被举报玩家是否成为护眼大队
	 */
	public PlayerDynamicEntity getToPlayerIdentity(long toPlayerId,Integer protectEyesTitleId) {
		Criteria criteria = new Criteria(_ID).is(toPlayerId).and(ITEMTITLE).is(protectEyesTitleId);
		Query query = new Query(criteria); 
		return findOne(query);
	}
	
	/**
	 * 增加 发送动态数量
	 * @param playerId
	 * @param num
	 */
	public void addSendDynamicNum(long playerId, int num) {
		Criteria Criteria = new Criteria(_ID).is(playerId);
		super.inc(new Query(Criteria), SEND_DYNAMIC_NUM, num);
	} 
	/**佩戴成就徽章*/
	public void	wearAchievementBadges(int id,long playerId) {
		Criteria Criteria = new Criteria(_ID).is(playerId);
		Query query = new Query(Criteria); 
		Update update = new Update();
		update.set(ACHIEVEMENT_TAGS, id);
		updateFirst(query, update);
	}
	/**佩戴头像框*/
	public void	wearAvatarFrame(int id,long playerId) {
		Criteria Criteria = new Criteria(_ID).is(playerId);
		Query query = new Query(Criteria); 
		Update update = new Update();
		update.set(AVATAR_FRAME, id);
		updateFirst(query, update);
	}
	/**取消护眼大队*/
	public void removeTitle(int titleId,long playerId) {
		Criteria Criteria = new Criteria(_ID).is(playerId);
		Query query = new Query(Criteria); 
		Update update = new Update();
		update.pull(ITEMTITLE, titleId);
		updateFirst(query, update);
	}
	/**
	 * 增加 回复动态数量
	 * @param playerId
	 * @param num
	 */
	public void addReplyDynamicNum(long playerId, int num) {
		Criteria Criteria = new Criteria(_ID).is(playerId);
		super.inc(new Query(Criteria), REPLY_DYNAMIC_NUM, num);
	}
	
	/**
	 * 查询所有玩家
	 */
	public List<PlayerDynamicEntity> getPlayerAllData(int page,int count){
		int offset = (page - 1) * count;
		int limit = count;
		Query query = new Query();
		query.with(new Sort(new Order(Direction.DESC,CLUE)));
		query.skip(offset).limit(limit);
		return find(query);
	}
	
	/**
	 * 查询玩家总数
	 */
	public long findPlayerCount() {
		Query query = new Query();
		return super.count(query);
	}
	
	/**
	 * 根据ID查询玩家信息
	 */
	public PlayerDynamicEntity findByIdPlayerData(String roleId) {
		Criteria Criteria = new Criteria(ROLE_ID).is(roleId);
		Query query = new Query(Criteria);
		return findOne(query);
	}
	
	/**
	 * 根据昵称查询玩家信息
	 */
	public List<PlayerDynamicEntity> findByNickNameData(String nickName, int page, int count) {
		int offset = (page - 1) * count;
		int limit = count;
		Criteria Criteria = new Criteria(NICK_NAME).is(nickName);
		Query query = new Query(Criteria);
		query.with(new Sort(new Order(Direction.DESC,CLUE)));
		query.skip(offset).limit(limit);
		return find(query);
	}
	
	/**
	 * 查询指定昵称玩家总数
	 */
	public long findByNamePlayerCount(String nickName) {
		Criteria Criteria = new Criteria(NICK_NAME).is(nickName);
		Query query = new Query(Criteria);
		return super.count(query);
	}
	
	/**
	 * 改变作者的好感度总数
	 */
	public void updateGoodFeeling(long playerId,long goodFeeling) {
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query=new Query().addCriteria(criteria);
		Update update = new Update();
		update.set(GOODFEELINGNUM, goodFeeling);
		super.updateInsert(query, update);
	}
	
	/**
	 * 改变玩家玩过的剧本总数
	 */
	public void updateScenarioNum(long playerId, int num) {
		Criteria criteria = new Criteria(_ID).is(playerId);
		Query query=new Query().addCriteria(criteria);
		Update update = new Update();
		update.set(SCENARIONUM, num);
		super.updateInsert(query, update);
	}
	
	public void test() {
		Criteria critera = new Criteria("_id").is(10100);
		Query query = new Query(critera);
		Update update = new Update();
		update.set("fansCount",2);
		super.updateFirst(query, update);
	}
}