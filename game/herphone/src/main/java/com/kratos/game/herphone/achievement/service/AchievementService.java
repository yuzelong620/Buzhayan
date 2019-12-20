package com.kratos.game.herphone.achievement.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.AchBadge_Json;
import com.globalgame.auto.json.AchDebris_Json;
import com.globalgame.auto.json.AchievementAward_Json;
import com.globalgame.auto.json.GameCatalog_Json;
import com.globalgame.auto.json.Tag_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.achievement.bean.ResAchAndClueBean;
import com.kratos.game.herphone.achievement.bean.ResAchievementData;
import com.kratos.game.herphone.achievement.entity.AchievementEntity;
import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.AchBadgeCache;
import com.kratos.game.herphone.json.datacache.AchDebrisCache;
import com.kratos.game.herphone.json.datacache.AchievementAwardCache;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.json.datacache.TagCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class AchievementService extends BaseService{	
	public AchievementEntity load(long playerId) {
		AchievementEntity achievementEntity = achievementDao.findByID(playerId);
		if (achievementEntity == null) {
			achievementEntity = new AchievementEntity();
			achievementEntity.setPlayerId(playerId);
			achievementDao.save(achievementEntity);
		}
		return achievementEntity;
	}
	/**获得新成就碎片*/
	public ResAchievementData addAchievement(int id) {
		AchDebrisCache cache = JsonCacheManager.getCache(AchDebrisCache.class);
		AchDebris_Json achDebris_Json = cache.getData(id);	
		ResAchievementData resAchievementData = new ResAchievementData();
		if (achDebris_Json == null) {
			throw new BusinessException("参数错误");
		}
		Player player =PlayerContext.getPlayer();
		AchievementEntity achievementEntity = load(player.getId());
		if (achievementEntity.getAchievementIds().contains(achDebris_Json.getId())) {
			resAchievementData.setGetState(false);
			return resAchievementData;
		}		
		//用原子操作更新字段
		achievementEntity.getAchievementIds().add(achDebris_Json.getId());
		achievementDao.updateAchievementIds(achievementEntity.getPlayerId(),achDebris_Json.getId());
		AchBadge_Json achBadge_Json = JsonCacheManager.getCache(AchBadgeCache.class).getData(achDebris_Json.getFromBadgeId());
		playerTagsService.add(achBadge_Json.getFromTagId(),player);
		List<Integer> achdebrisList = cache.getAchDebrisId(achDebris_Json.getFromBadgeId());
		if (checkBadge(achdebrisList,achievementEntity.getAchievementIds())) {
			//用原子操作更新字段
			achievementDao.updateBadgeIds(achievementEntity.getPlayerId(), achDebris_Json.getFromBadgeId());			
		}
		/**重新计算成就碎片数*/
		PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(player.getId());
		
		//成就素片数
		int achievementNum = commonService.recountAchievement(player, playerDynamicEntity);
		AchievementAwardCache achievementAwardCache = JsonCacheManager.getCache(AchievementAwardCache.class);
		//获取对应的奖励信息
		AchievementAward_Json achievementAward_Json = achievementAwardCache.getData(achievementNum);
		//如果到了应该发奖励的时候
		if(achievementAward_Json != null){
			//设置成功获得碎片
			resAchievementData.setGetState(true);
			//获取获得的物品ID
			resAchievementData.setItemId(achievementAward_Json.getReward().get(0).getKey()); 
			//增加奖励
			commonService.add(player.getId(), achievementAward_Json.getReward()); 
		}
		taskService.playerScheduleUpdate(CommonCost.TackType.achievementDebris.ordinal(),1); //任务接口(任务类型,完成次数)
		//更新阶段任务，调用阶段任务更新接口，获得碎片
		stageTaskService.updateTaskValue(CommonCost.StageTaskType.achievementDebris.ordinal(), 1);
		return resAchievementData;
	}
	/**判断小徽章是否收集完成*/
	private boolean checkBadge(List<Integer> achdebrisList,Set<Integer> achievementIds) {
		for (Integer achdebrisId : achdebrisList) {
			if (!achievementIds.contains(achdebrisId)) {
				return false;
			}
		}
		return true;
	}
	/**该徽章收集进度*/
	public int badgeProgress(List<Integer> achdebrisList,Set<Integer> achievementIds) {	
		int count = 0;
		for (Integer id : achdebrisList) {
			if (achievementIds.contains(id)) {
				count++;
			}
		}
		return count;
	}
	public int getAchievementNum(long playerId) {
		AchievementEntity achievementEntity = load(playerId);
		AchDebrisCache cache = JsonCacheManager.getCache(AchDebrisCache.class);		
		int AchievementNum = 0;
		for (Integer achievementId : achievementEntity.getAchievementIds()) {
			AchievementNum += cache.getData(achievementId).getGetfragment();
		}
		return AchievementNum;
	}
	/**添加标签并获得对应的奖励*/
	public void addTag(long playerId,int tagId) {
		Tag_Json tag_Json = JsonCacheManager.getCache(TagCache.class).getData(tagId);	
		if (tag_Json == null ) {
			log.error("老用户获得Tag失败，找不到tagId");
			return;
		}
		AchievementEntity achievementEntity = achievementService.load(playerId);		
		if (achievementEntity.getTags().contains(tagId)) {
			log.error("该成就标签已被解锁,玩家Id:"+playerId+"标签Id:"+tagId);
			return;
		}
		achievementDao.updateTags(playerId, tagId);		
	}
	public Set<Integer> listAchievement(int gameId) {
		GameCatalogCache cache=JsonCacheManager.getCache(GameCatalogCache.class);
		GameCatalog_Json game=cache.getData(gameId);
		if(game==null){
			throw new BusinessException("参数错误");
		}
		List<AchDebris_Json> lists= JsonCacheManager.getCache(AchDebrisCache.class).getList();
		Set<Integer> set = new HashSet<Integer>();
		Player player = PlayerContext.getPlayer();
		AchievementEntity achievementEntity = load(player.getId());
		for (AchDebris_Json achDebris_Json : lists) {
			if (achDebris_Json.getGameid() == gameId) {
				set.add(achDebris_Json.getId());
			}
		}
		Set<Integer> reSet = new HashSet<>();
		reSet.addAll(set);
		reSet.retainAll(achievementEntity.getAchievementIds());
		return reSet;
	}
	public ResAchAndClueBean reslistAchAndClue(int gameId) {
		Set<Integer> set = listAchievement(gameId);
		List<Integer> list = playerClueService.getMyClues(gameId);
		return new ResAchAndClueBean(list, set);
	}
}
