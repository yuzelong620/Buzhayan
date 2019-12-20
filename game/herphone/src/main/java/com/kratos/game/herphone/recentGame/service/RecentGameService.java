package com.kratos.game.herphone.recentGame.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.recentGame.entity.RecentGameEntity;

@Service
public class RecentGameService extends BaseService{
	/**
	 * 加载方法
	 */
	 public RecentGameEntity load(long playerId) {
		 RecentGameEntity recentGameEntity = recentGameDao.findByID(playerId);
		 if (recentGameEntity  == null) {
			recentGameEntity = new RecentGameEntity();
			recentGameEntity.setPlayerId(playerId);
			List<Integer> list = playerExplorationService.getGameid(playerId);	
			 if (list != null) {
				 recentGameEntity.setList(list);
			}	
			recentGameDao.save(recentGameEntity);
		}	
		 return recentGameEntity;
	}
	 /**
	  * 更新最近游戏
	  */
	public void updateRecentGameEntity(long playerId, int gameId) {
		Integer value = gameId;
		RecentGameEntity recentGameEntity = load(playerId);
		List<Integer> list = recentGameEntity.getList();
		list.remove(value);
		list.add(0, value);
		if (list.size()>6) {
			list.remove(6);
		}
		recentGameEntity.setList(list);
		recentGameDao.save(recentGameEntity);
	}
	/**
	 * 重载加载方法 ，用于获取自己最近游戏列表
	 */
	public RecentGameEntity load() {
		 Player player = PlayerContext.getPlayer();
		 RecentGameEntity recentGameEntity = recentGameDao.findByID(player.getId());
		 if (recentGameEntity  == null) {
			recentGameEntity = new RecentGameEntity();
			recentGameEntity.setPlayerId(player.getId());
			List<Integer> list = playerExplorationService.getGameid(player.getId());	
			 if (list != null) {
				 recentGameEntity.setList(list);
			}	
			recentGameDao.save(recentGameEntity);
		}	
		 return recentGameEntity;
	}
}
