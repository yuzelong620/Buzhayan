package com.kratos.game.herphone.gift.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.CostCurrency_Json;
import com.globalgame.auto.json.Gift_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.gift.bean.GiftBean;
import com.kratos.game.herphone.gift.bean.RanKingBean;
import com.kratos.game.herphone.gift.bean.ResRanKing;
import com.kratos.game.herphone.gift.entity.GiftEntity;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.CostCurrencyCache;
import com.kratos.game.herphone.json.datacache.GiftCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

@Service
public class GiftService extends BaseService {

	private GiftEntity load(GiftBean giftBean, PlayerDynamicEntity sendPlayer) {
		GiftEntity giftEntity = giftDao.findByID(giftBean.getAuthorId() + "_" + sendPlayer.getPlayerId());
		if(giftEntity == null){
			giftEntity = new GiftEntity(giftBean.getAuthorId() + "_" + sendPlayer.getPlayerId(),
					sendPlayer.getPlayerId(),giftBean.getAuthorId(),0);
			giftDao.save(giftEntity);
		}
		return giftEntity;
	}
	
	/**
	 * 玩家送给作者礼物
	 */
	public void sendGift(GiftBean giftBean) {
		Player player = PlayerContext.getPlayer();
		PlayerDynamicEntity authorData = playerDynamicDao.findByID(giftBean.getAuthorId()); //作者信息
		PlayerDynamicEntity sendPlayer = playerDynamicDao.findByID(player.getId()); //赠送人信息
		BagEntity bagEntity = bagDao.findByID(player.getId());
		Map<Integer, Integer> map = bagEntity.getBagItems(); // 获取背包信息
		Integer item = map.get(giftBean.getItemId());
		CostCurrencyCache costCurrencyCache = JsonCacheManager.getCache(CostCurrencyCache.class);
		CostCurrency_Json costCurrency_Json = costCurrencyCache.getData(giftBean.getItemId());
		if (authorData == null) {
			throw new BusinessException("作者不存在");
		}
		if(item == null || item == 0){
			if(sendPlayer.getCurrency() < costCurrency_Json.getCostNum()){
				throw new BusinessException("资源不足");
			}
		}
		GiftCache giftCache = JsonCacheManager.getCache(GiftCache.class);
		Gift_Json gift_Json = giftCache.getData(giftBean.getItemId()); //通过物品ID查询礼物信息(增加好感度值)
		GiftEntity giftEntity = load(giftBean,sendPlayer);
		//减少背包物品
		if(item != null && item >= 1){
			item = item - giftBean.getGiftNum();
			map.put(giftBean.getItemId(),item);
		} else {
			playerDynamicDao.updateCurrency(player.getId(),0-gift_Json.getCost());
		}
		bagEntity.setBagItems(map);
		bagDao.save(bagEntity); //更新背包
		long goodFeeling = gift_Json.getValue() * giftBean.getGiftNum();
		//改变该玩家对该作者的好感度总数
		giftDao.updateGoodFeeling(giftBean.getAuthorId() + "_" + sendPlayer.getPlayerId(),giftEntity.getGoodFeeling() + goodFeeling);
		//改变作者好感度总数
		playerDynamicDao.updateGoodFeeling(giftBean.getAuthorId(),authorData.getGoodFeelingNum() + goodFeeling);
		//增加好感度,调用任务更新接口
		taskService.playerScheduleUpdate(CommonCost.TackType.goodFeeling.ordinal(),(int) goodFeeling);
		//打赏礼物，调用阶段任务更新接口
		stageTaskService.updateTaskValue(CommonCost.StageTaskType.sendPresents.ordinal(), 1);
	}


	/**
	 * 礼物排行榜
	 */
	public ResRanKing giftRinKingList(long authorId) {
		Player player = PlayerContext.getPlayer();
		List<GiftEntity> giftEntityList = giftDao.findBySendPlayerId(authorId);
		List<RanKingBean> ranKingBeanList = new ArrayList<RanKingBean>();
		int thisPlayerRanKing = -1; //默认未上榜
		long goodFeeling = 0;
		for (int i = 0; i < giftEntityList.size(); i++) {
			RanKingBean ranKingBean = new RanKingBean();
			ranKingBean.setSendGiftPlayerId(String.valueOf(giftEntityList.get(i).getSendGiftPlayerId()));
			ranKingBean.setAuthorId(String.valueOf(giftEntityList.get(i).getAuthorId()));
			ranKingBean.setGoodFeeling(giftEntityList.get(i).getGoodFeeling());
			PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(giftEntityList.get(i).getSendGiftPlayerId());
			ranKingBean.setNickName(playerDynamicEntity.getNickName());
			ranKingBean.setHeadImgUrl(playerDynamicEntity.getHeadImgUrl());
			ranKingBean.setSex(playerDynamicEntity.getSex());
			ranKingBean.setAchievementTags(playerDynamicEntity.getAchievementTags());
			ranKingBean.setAvatarFrame(playerDynamicEntity.getAvatarFrame());
			ranKingBeanList.add(ranKingBean);
			if(giftEntityList.get(i).getSendGiftPlayerId() == player.getId()){
				thisPlayerRanKing = i+1; //记录该玩家的排名信息
				goodFeeling = giftEntityList.get(i).getGoodFeeling();
			}
		}
		ResRanKing ResRanKing = new ResRanKing(thisPlayerRanKing,String.valueOf(goodFeeling),ranKingBeanList);
		return ResRanKing;
	}

}
