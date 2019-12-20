package com.kratos.game.herphone.helperMessage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.globalgame.auto.json.VideoReward_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.helperMessage.bean.ReqOptionAnswer;
import com.kratos.game.herphone.helperMessage.bean.ResRecordOption;
import com.kratos.game.herphone.helperMessage.bean.ReturnMessage;
import com.kratos.game.herphone.helperMessage.entity.PlayerHelperMessageEntity;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.VideoRewardCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.util.DateUtil;

@Component
public class HelperMessageService extends BaseService {

	/**
	 * 小罗助手推送
	 * 
	 * @return 小罗助手推送消息
	 */
	public PlayerHelperMessageEntity load(long playerId) {
		//String id = createId(playerId);
		PlayerHelperMessageEntity playerHelperMessageEntity = playerHelperAnswerDao.findByID(playerId);
		if (playerHelperMessageEntity == null) {
			List<Integer> videoAwardIds = new ArrayList<Integer>();
			VideoRewardCache cache=JsonCacheManager.getCache(VideoRewardCache.class);
			VideoReward_Json videoReward_Json=cache.getVideoReward_Json(1);
			long nowDate = System.currentTimeMillis();
			playerHelperMessageEntity = new PlayerHelperMessageEntity(playerId, videoReward_Json.getId(), 0, 1,nowDate, videoAwardIds);
			playerHelperAnswerDao.save(playerHelperMessageEntity);
		}
		return playerHelperMessageEntity;
	}

	/**
	 * 小罗助手推送
	 * 
	 * @return 小罗助手推送消息
	 */
	public ReturnMessage getInformation() {
		Player player = PlayerContext.getPlayer();
		PlayerHelperMessageEntity playerHelperMessageEntity = load(player.getId());
		examineDate(playerHelperMessageEntity);
			VideoRewardCache cache=JsonCacheManager.getCache(VideoRewardCache.class);
			VideoReward_Json videoReward_Json=cache.getVideoReward_Json(playerHelperMessageEntity.getNowManyDays());
			ReturnMessage returnMessage = new ReturnMessage(videoReward_Json.getId(), videoReward_Json.getVideoUrl(), playerHelperMessageEntity.getVideoWatchState(), videoReward_Json.getDay(),videoReward_Json.getPictrue());
			return returnMessage;
	}

	//检查日期
	private void examineDate(PlayerHelperMessageEntity playerHelperMessageEntity) {
		long nowDate = System.currentTimeMillis();
		long time = playerHelperMessageEntity.getNowDate();
		//不是同一天
		if(!DateUtil.isSameDate(nowDate, time)) {
			VideoRewardCache cache=JsonCacheManager.getCache(VideoRewardCache.class);
			VideoReward_Json videoReward_Json=cache.getVideoReward_Json(playerHelperMessageEntity.getNowManyDays()+1);
			if(videoReward_Json != null){
				playerHelperMessageEntity.setVideoAwardId(videoReward_Json.getId());
				playerHelperMessageEntity.setNowDate(nowDate);
				playerHelperMessageEntity.setNowManyDays(playerHelperMessageEntity.getNowManyDays()+1);
				playerHelperMessageEntity.setVideoWatchState(0);
			}
			playerHelperAnswerDao.save(playerHelperMessageEntity);
			
		}
	}

	/**
	 *  玩家视频观看完成回复小罗助手消息
	 * 
	 * @param reqOptionAnswer
	 */
	public ResRecordOption recordOption(ReqOptionAnswer reqOptionAnswer) {
		//判断玩家是否观看完成视频
		if (reqOptionAnswer.getVideoAwardId() == -1) {
			throw new BusinessException("视频未完成观看");
		}
		Player player = PlayerContext.getPlayer();
		PlayerHelperMessageEntity playerHelperMessageEntity = load(player.getId());
		//检测日期是否是同一天
		examineDate(playerHelperMessageEntity);
		//判断数据库中是否有视频奖励Id
		if (playerHelperMessageEntity.getVideoAwardIds().contains(reqOptionAnswer.getVideoAwardId())) {
			throw new BusinessException("不能重复领取");
		} 
		//判断今天是否领取过奖励
		if(playerHelperMessageEntity.getVideoWatchState() == 1) {
			throw new BusinessException("不能重复领取");
		}
		VideoRewardCache cache=JsonCacheManager.getCache(VideoRewardCache.class);
		VideoReward_Json videoReward_Json=cache.getVideoReward_Json(playerHelperMessageEntity.getNowManyDays());
		//消息回复完成增加体力
		player.addPower(videoReward_Json.getPower(), true);
	    playerServiceImpl.cacheAndPersist(player.getId(), player);
	    //视频观看完成状态 默认0未完成观看 1完成观看
	    playerHelperMessageEntity.setVideoWatchState(1);
		playerHelperMessageEntity.getVideoAwardIds().add(reqOptionAnswer.getVideoAwardId());
		playerHelperAnswerDao.save(playerHelperMessageEntity);
		return new ResRecordOption(videoReward_Json.getPower());
	}
}
