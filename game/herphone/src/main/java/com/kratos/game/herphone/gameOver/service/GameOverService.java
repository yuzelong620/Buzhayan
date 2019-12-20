package com.kratos.game.herphone.gameOver.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Switch;
import org.springframework.stereotype.Service;

import com.globalgame.auto.json.GameOver_Json;
import com.kratos.game.herphone.achievement.entity.AchievementEntity;
import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.gameOver.bean.ResGameOverInfo;
import com.kratos.game.herphone.gamemanager.bean.ResGameInfo;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.AchDebrisCache;
import com.kratos.game.herphone.json.datacache.BoxCache;
import com.kratos.game.herphone.json.datacache.GameOverCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.mind.core.util.IntTuple;
@Service
public class GameOverService extends BaseService{
	
	/**
	 * 游戏结束界面提示
	 */
	
	public ResGameOverInfo gameOver() {
		GameOverCache cache = JsonCacheManager.getCache(GameOverCache.class);
		//获取玩家信息
		Player player = PlayerContext.getPlayer();
		//获取玩家游戏信息
		PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(player.getId());
		//获取玩家游戏次数信息
		int scenarioNum = playerDynamicEntity.getScenarioNum()+1 ;
		//实例化返回对象
		ResGameOverInfo result = new ResGameOverInfo();
		//获取该玩家奖励内容
		GameOver_Json gameOver_Json = cache.getData(scenarioNum );
		//调用获取奖励方法
		if(gameOver_Json != null) {
			addAward(player,gameOver_Json);
			//设置玩家获得奖励
			result.setAward(gameOver_Json.getName());
		}
		//更新玩家游戏次数
		playerDynamicDao.updateScenarioNum(player.getId(), scenarioNum);
		//设置玩家通过副本次数
		result.setScenarioNum(scenarioNum);
		//设置玩家线索值
		result.setClue(playerDynamicEntity.getClue());
		return result;
		
	}
	/**
	 * 奖励物品添加到玩家账号中
	 * @param player
	 * @param gameOver_Json
	 */
	public void addAward(Player player,GameOver_Json gameOver_Json) {
		//判断奖励类型
		int awardType = gameOver_Json.getType();
		List<IntTuple> awardValue = gameOver_Json.getValue();
		switch (awardType) {
		case 4:
			//类型为4,奖励为徽章,徽章加入到玩家徽章表中
			addBadge(player,gameOver_Json.getValue().get(0).getKey());
			break;
		case 5:
			//类型为5,奖励为头像框,讲头像框加入玩家背包中
			for (IntTuple intTuple : awardValue) {
				addToBag(player,intTuple.getKey(),intTuple.getValue());
			}
			break;
		case 6:
			//类型为5,奖励为背包物品,如鲜花等
			for (IntTuple intTuple : awardValue) {
				addToBag(player,intTuple.getKey(),intTuple.getValue());
			}
			break;
		case 7 :
			//类型为7,奖励为宝箱
			for (IntTuple intTuple : awardValue) {
				addToBag(player,intTuple.getKey(),intTuple.getValue());
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 获取徽章,将辉章加入到玩家徽章中
	 */
	public void addBadge(Player player,int badgeId) {
		//获取玩家徽章
		AchievementEntity  achievementEntity = achievementService.load(player.getId());
		//查询该小徽章对应的碎片
		AchDebrisCache achDebrisCache = JsonCacheManager.getCache(AchDebrisCache.class);
		List<Integer> achdebrisList = achDebrisCache.getAchDebrisId(badgeId);
		//查询该玩家拥有的碎片
		Set<Integer> achievementIds = achievementEntity.getAchievementIds();
		//将该小徽章碎片加入玩家拥有碎片中
		achievementIds.addAll(achdebrisList);
		//查询玩家拥有的小徽章
		Set<Integer> badgeIds = achievementEntity.getBadgeIds();
		//将该徽章加入玩家拥有徽章中
		badgeIds.add(badgeId);
		//将玩家徽章数据和碎片数据存入数据库
		achievementEntity.setBadgeIds(badgeIds);
		achievementEntity.setAchievementIds(achievementIds);
		achievementDao.save(achievementEntity);
	}
	/**
	 * 将物品类奖品加入玩家背包中
	 * @param player
	 * @param awardId
	 * @param num
	 */
	public void addToBag(Player player,int awardId,int num) {
		//获取玩家背包信息
		BagEntity bagEntity = bagService.load(player.getId());
		//获取背包物品
		Map<Integer, Integer> bagItems = bagEntity.getBagItems();
		//
		Integer item = bagItems.get(awardId);
		if(item == null) {
			bagItems.put(awardId, num);
			bagDao.save(bagEntity);
		}else {
			item += num;
			bagItems.put(awardId, item);
			bagDao.save(bagEntity);
		}
	}
}
