package com.kratos.game.herphone.gamemanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.globalgame.auto.json.GameCatalog_Json;
import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.game.herphone.cache.AppCache;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.gamemanager.bean.ResGameInfo;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;

@Service
public class GameScoreService extends BaseService{
	/**查看剧本评分详细信息*/
	@SuppressWarnings("unchecked")
	public List<ResGameInfo> listGameScore() {
		List<ResGameInfo> reslist = JedisUtils.getInstance().get(AppCache.gamePeopleNum, List.class);
		if (reslist == null) {
			reslist = new ArrayList<>();
			List<GameCatalog_Json> list = JsonCacheManager.getCache(GameCatalogCache.class).getList();
			Map<Integer, String> map = scoreService.getAllScore();
			Map<Integer, Integer> score = scoreService.getScorePeopleNum();
			Map<Integer, Integer> gamePlayer = playerExplorationService.getGamePlayerNum();	
			for (GameCatalog_Json gameCatalog_Json : list) {
				if (map.containsKey(gameCatalog_Json.getId())) {
					ResGameInfo resGameInfo = new ResGameInfo();
					resGameInfo.setGameId(gameCatalog_Json.getId());
					resGameInfo.setGameName(gameCatalog_Json.getName());
					resGameInfo.setScore(map.get(gameCatalog_Json.getId()));
					if (score.containsKey(gameCatalog_Json.getId())) {
						resGameInfo.setDiscussPeopleNum(score.get(gameCatalog_Json.getId()));					
					}else {
						resGameInfo.setDiscussPeopleNum(0);
					}	
					if (gamePlayer.containsKey(gameCatalog_Json.getId())) {
						resGameInfo.setPlayPeopleNum(gamePlayer.get(gameCatalog_Json.getId()));
					}else {
						resGameInfo.setDiscussPeopleNum(0);
					}	
					reslist.add(resGameInfo);
				}
			}
			  JedisUtils.getInstance().set(AppCache.gamePeopleNum, reslist);
		}		
		return reslist;
	}
}
