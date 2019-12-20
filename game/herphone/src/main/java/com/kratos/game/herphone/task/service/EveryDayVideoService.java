package com.kratos.game.herphone.task.service;

import org.springframework.stereotype.Component;

import com.globalgame.auto.json.EveryDayVideo_Json;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.EveryDayVideoCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.task.bean.EveryDayVideoBean;
import com.kratos.game.herphone.task.entity.EveryDayVideoEntity;
import com.kratos.game.herphone.util.DateUtil;

@Component
public class EveryDayVideoService extends BaseService{

	public EveryDayVideoEntity load(long playerId) {
		EveryDayVideoEntity everyDayVideoEntity = everyDayVideoDao.findByID(playerId);
		if (everyDayVideoEntity == null) {
			EveryDayVideoCache cache = JsonCacheManager.getCache(EveryDayVideoCache.class);
			EveryDayVideo_Json everyDayVideoCache = cache.getEveryDayVideo_Jsons(1);
			long nowDate = System.currentTimeMillis();
			everyDayVideoEntity = new EveryDayVideoEntity(playerId, everyDayVideoCache.getId(), 1,nowDate);
			everyDayVideoDao.save(everyDayVideoEntity);
		}
		return everyDayVideoEntity;
	}
	
	/**
	 * 用户获取每日激励视频
	 */
	public EveryDayVideoBean getEveryDayVideo() {
		Player player = PlayerContext.getPlayer();
		EveryDayVideoEntity everyDayVideoEntity = load(player.getId());
		examineDate(everyDayVideoEntity);
		EveryDayVideoCache cache = JsonCacheManager.getCache(EveryDayVideoCache.class);
		EveryDayVideo_Json everyDayVideo_Json = cache.getEveryDayVideo_Jsons(everyDayVideoEntity.getNowManyDays());
		return new EveryDayVideoBean(everyDayVideo_Json.getId(), everyDayVideo_Json.getVideoUrl(), everyDayVideo_Json.getDay(),everyDayVideo_Json.getPictrue());
	}
	
	//检查日期
	private void examineDate(EveryDayVideoEntity everyDayVideoEntity) {
		long nowDate = System.currentTimeMillis();
		long time = everyDayVideoEntity.getNowDate();
		//不是同一天
		if(!DateUtil.isSameDate(nowDate, time)) {
			EveryDayVideoCache cache = JsonCacheManager.getCache(EveryDayVideoCache.class);
			EveryDayVideo_Json everyDayVideo_Json = cache.getEveryDayVideo_Jsons(everyDayVideoEntity.getNowManyDays()+1);
			if(everyDayVideo_Json != null){
				everyDayVideoEntity.setVideoAwardId(everyDayVideo_Json.getId());
				everyDayVideoEntity.setNowDate(nowDate);
				everyDayVideoEntity.setNowManyDays(everyDayVideoEntity.getNowManyDays()+1);
			}
			everyDayVideoDao.save(everyDayVideoEntity);
		}
	}
}
