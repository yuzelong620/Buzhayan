package com.kratos.game.herphone.badge.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.AchBadge_Json;
import com.globalgame.auto.json.Item_Json;
import com.globalgame.auto.json.Tag_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.achievement.entity.AchievementEntity;
import com.kratos.game.herphone.badge.bean.ResBadgeBean;
import com.kratos.game.herphone.badge.bean.ResBadgeInfo;
import com.kratos.game.herphone.badge.bean.ResReceiveState;
import com.kratos.game.herphone.badge.entity.BadgeEntity;
import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.ReceiveState;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.AchBadgeCache;
import com.kratos.game.herphone.json.datacache.AchDebrisCache;
import com.kratos.game.herphone.json.datacache.ItemCache;
import com.kratos.game.herphone.json.datacache.TagCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;

import lombok.extern.log4j.Log4j;
@Log4j
@Service
public class BadgeService extends BaseService{
	private static BadgeService instance;
	public BadgeService() {
		instance = this;
	}
	
	public static BadgeService getInstance() {
		return instance;
	}
	@Deprecated
	public void init() {
		AchBadgeCache cache =JsonCacheManager.getCache(AchBadgeCache.class);
		List<AchBadge_Json> jsons = cache.getList();		
		for (AchBadge_Json achBadge_Json : jsons) {
			BadgeEntity badgeEntity = badgeDao.findByID(achBadge_Json.getId());
			if (badgeEntity != null) {
				continue;
			}
			badgeEntity = new BadgeEntity();
			badgeEntity.setId(achBadge_Json.getId());
			badgeEntity.setUnlockNum(0);
			badgeDao.save(badgeEntity);
		}
	}
	/**根据tagId查看徽章获得进度*/
	public ResBadgeInfo BadgeProgress(int tagId) {
		AchBadgeCache cache =JsonCacheManager.getCache(AchBadgeCache.class);
		List<Integer> list = cache.getAchBadgeId(tagId);		
		if (list == null ||list.size() == 0) {
			throw new BusinessException("参数错误");
		}
		Player player = PlayerContext.getPlayer();
		AchievementEntity  achievementEntity = achievementService.load(player.getId());
		if (achievementEntity.getTags().contains(tagId)) {
			ResBadgeInfo resBadgeInfo = listResBadgeBean(tagId, list, achievementEntity);
			resBadgeInfo.setState(ReceiveState.receive.ordinal());		
			return resBadgeInfo;
		}else {
			return listResBadgeBean(tagId, list, achievementEntity);
		}
	}
	private ResBadgeInfo listResBadgeBean(int tagId,List<Integer> list,AchievementEntity  achievementEntity) {
		AchDebrisCache achDebrisCache = JsonCacheManager.getCache(AchDebrisCache.class);
		
		List<ResBadgeBean> reslist = new ArrayList<>();
		ResBadgeInfo resBadgeInfo = new ResBadgeInfo();
		resBadgeInfo.setState(ReceiveState.notReceive.ordinal());
		for (Integer badgeId : list) {
			List<Integer> achdebrisList = achDebrisCache.getAchDebrisId(badgeId);		
			ResBadgeBean resBadgeBean = new ResBadgeBean();
			resBadgeBean.setId(badgeId);		
			if (achievementEntity.getBadgeIds().contains(badgeId)) {
				resBadgeBean.setBadgeProgress(achdebrisList.size());
				resBadgeBean.setCountProgress(achdebrisList.size());
			}else {
				int count = achievementService.badgeProgress(achdebrisList, achievementEntity.getAchievementIds());
				resBadgeBean.setBadgeProgress(count);
				resBadgeBean.setCountProgress(achdebrisList.size());
				resBadgeInfo.setState(ReceiveState.undone.ordinal());
			}
			reslist.add(resBadgeBean);
		}
		resBadgeInfo.setList(reslist);
		return resBadgeInfo;
	}
	/**领取奖励*/
	public void receiveTagReward(int tagId) {
		Tag_Json tag_Json = JsonCacheManager.getCache(TagCache.class).getData(tagId);	
		if (tag_Json == null ) {
			throw new BusinessException("参数错误");
		}
		Player player = PlayerContext.getPlayer();
		AchievementEntity achievementEntity = achievementService.load(player.getId());
		if (achievementEntity.getTags().contains(tagId)) {
			log.error("该成就标签已被解锁,玩家Id:"+player.getId()+"标签Id:"+tagId);
			return;
		}
		BagEntity bagEntity = bagService.load(player.getId());
		for (Integer itemId : tag_Json.getList()) {
			if (bagEntity.getBagItems().containsKey(itemId)) {
				log.error("此物品该玩家已经获得：玩家ID"+player.getId()+"物品Id："+itemId);
				return;
			}
			bagEntity.getBagItems().put(itemId, 1);
		}
		achievementDao.updateTags(player.getId(), tagId);
		bagDao.save(bagEntity);
	}
	/**成就徽章领取状态*/
	public List<ResReceiveState> receiveState() {
		List<Tag_Json> tag_Jsons = JsonCacheManager.getCache(TagCache.class).getList();
		Player player = PlayerContext.getPlayer();
		AchievementEntity achievementEntity = achievementService.load(player.getId());
		List<Item_Json> item_Jsons = JsonCacheManager.getCache(ItemCache.class).getList();
		BagEntity bagEntity = bagService.load(player.getId());
		Set<Integer> set = bagEntity.getBagItems().keySet();
		List<ResReceiveState> reslist = new ArrayList<>();
		for (Item_Json item_Json : item_Jsons) {
			ResReceiveState resReceiveState = new  ResReceiveState();
			if (set.contains(item_Json.getId())) {
				//已拥有
				resReceiveState.setItemId(item_Json.getId());
				resReceiveState.setState(ReceiveState.receive.ordinal());
				reslist.add(resReceiveState);
			}else {
				//判断状态
				for (Tag_Json tag_Json : tag_Jsons) {
					if (tag_Json.getList().contains(item_Json.getId())) {
						if (achievementEntity.getBadgeIds().containsAll(tag_Json.getLittlename())) {
							resReceiveState.setItemId(item_Json.getId());
							resReceiveState.setState(ReceiveState.notReceive.ordinal());
							reslist.add(resReceiveState);
						}else {
							resReceiveState.setItemId(item_Json.getId());
							resReceiveState.setState(ReceiveState.undone.ordinal());
							reslist.add(resReceiveState);
						}
					}
				}		
			}
		}
		return reslist;
	}
}
