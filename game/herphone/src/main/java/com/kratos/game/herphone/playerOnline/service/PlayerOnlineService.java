package com.kratos.game.herphone.playerOnline.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.playerOnline.bean.PlayerTimeState;
import com.kratos.game.herphone.playerOnline.entity.PlayerOnlineEntity;

@Component
public class PlayerOnlineService extends BaseService {

	public PlayerOnlineEntity load(long playerId) {
		PlayerOnlineEntity OnlineEntity = playerOnlineDao.findByID(playerId);
		if (OnlineEntity == null) {
			OnlineEntity = new PlayerOnlineEntity(playerId);
			playerOnlineDao.save(OnlineEntity);
		}
		return OnlineEntity;
	}

	/**
	 * 更新玩家在线时长 监听玩家是否在线 每一分钟更新一次在线时长 如果玩家超过65秒没有发送在线请求 则表示离线 不再更新在线时长和检测玩家消息状态
	 * 
	 * @return 返回玩家消息状态和系统时间
	 */
	public PlayerTimeState update() {
		Player player = PlayerContext.getPlayer();
		PlayerOnlineEntity playerOnlineEntity = load(player.getId()); 
		long nowTime = System.currentTimeMillis();
		long onTime = nowTime - playerOnlineEntity.getLastTime();
		long deferTime = (onTime - (1 * 60 * 1000));
		if (onTime > 1 * 60 * 1000) {
			// 超过1分钟以后再次判断玩家请求是否在没有超过5秒 如果没有都按照网络延迟计算 后续继续更新在线时长
			if (deferTime > 0 && deferTime < 5000) {
				playerOnlineEntity.setOnlineTime(playerOnlineEntity.getOnlineTime() + onTime - deferTime);
				playerOnlineEntity.setLastTime(nowTime);
			} else {
				playerOnlineEntity.setLastTime(nowTime);
			}
		} else {
			playerOnlineEntity.setOnlineTime(playerOnlineEntity.getOnlineTime() + onTime);
			playerOnlineEntity.setLastTime(nowTime);
		}
		playerOnlineDao.save(playerOnlineEntity);
		
		
		int messageReadPointState = messasgeService.getPlayerUnreadState(player.getId());//消息红点
		if (messageReadPointState == 0) {
			messageReadPointState = systemMessgaeService.getPlayerUnreadState(player.getId());
		}
	    int   replyReadPointState= discussService.getPlayerUnreadReplyState(player.getId());//回复红点
		return new PlayerTimeState(messageReadPointState,System.currentTimeMillis(),replyReadPointState);
	}
	/**
	 * 为定时任务提供查找累计时间达到72小时的方法
	 */
	public List<Long> getPlayerIds(List<Long> list) {
		List<PlayerOnlineEntity> playerOnlineEntities = playerOnlineDao.meet72HoursPlayer(list);
		List<Long> playerIds = new ArrayList<>();
		for (PlayerOnlineEntity playerOnlineEntity : playerOnlineEntities) {
			playerIds.add(playerOnlineEntity.getPlayerId());
		}
		return playerIds;
	}
	
	/**
	 * 根据playerId获取在线时长
	 */
	public long findOneByPlayerIdOnlineTime(long playerId) {
		PlayerOnlineEntity playerOnlineEntity = playerOnlineDao.findByID(playerId);
		if(playerOnlineEntity != null) {
			return playerOnlineEntity.getOnlineTime();
		} 
		return 0;
	}
	
	/**
	 * 根据playerId获取在线时长
	 */
	public Map<Long,String> findByPlayerIdOnlineTime(List<Long> playerId) {
		List<PlayerOnlineEntity> playerOnlineEntityList = playerOnlineDao.findByIdOnlineTime(playerId);
		Map<Long,String> map = new HashMap<Long, String>();
		for (PlayerOnlineEntity playerOnlineEntity : playerOnlineEntityList) {
			map.put(playerOnlineEntity.getPlayerId(), String.valueOf(playerOnlineEntity.getOnlineTime()));
		}
		return map;
	}
}
