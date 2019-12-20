package com.kratos.game.herphone.game.chose.service;

import java.util.List;
import java.util.Map;

import com.kratos.engine.framework.crud.ICrudService;
import com.kratos.game.herphone.game.chose.domain.PlayerExploration;
import com.kratos.game.herphone.player.bean.PlayerAstrictBean;

public interface PlayerExplorationService extends ICrudService<Long, PlayerExploration> {
	
	  List<Integer> getGameid(long playId);
	  int getGameCount(long playId);
	  Map<Long, Integer> getGameCount(List<Long> playerIds);
	  List<Long> listPlayerId(List<Long> playerIds);
	  /**获取剧本玩过的人数*/
	  Map<Integer, Integer> getGamePlayerNum();
}
