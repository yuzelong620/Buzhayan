package com.kratos.game.herphone.gameDispose.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.GameCatalog_Json;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.gameDispose.bean.HavePhoneGameBean;
import com.kratos.game.herphone.gameDispose.bean.NotHavePhoneGameBean;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.user.entity.UserEntity;

@Service
public class GameDisposeService extends BaseService{

	/**
	 * 查询绑定手机号机器人玩家的剧本信息
	 */
	public List<HavePhoneGameBean> getHavePhone(){
		List<GameCatalog_Json> gameCatalog_JsonList = JsonCacheManager.getCache(GameCatalogCache.class).getList();
		List<HavePhoneGameBean> gameDisposeBeanList = new ArrayList<HavePhoneGameBean>();
		Set<Integer> playerIdAll = new HashSet<Integer>();
		for (GameCatalog_Json gameCatalog_Json : gameCatalog_JsonList) {
			playerIdAll.add(gameCatalog_Json.getAuthorid());
		}
		List<UserEntity> userEntityList = userDao.findAllByPlayerId(playerIdAll);
		for (GameCatalog_Json gameCatalog_Json : gameCatalog_JsonList) {
			for (UserEntity userEntity : userEntityList) {
				if(Long.valueOf(gameCatalog_Json.getAuthorid()).equals(userEntity.getPlayerId())) {
					gameDisposeBeanList.add(new HavePhoneGameBean(
							gameCatalog_Json.getAuthorid().toString(),
							gameCatalog_Json.getName(),
							gameCatalog_Json.getAuthorName(),
							userEntity.getPhone()));
				}
			}
		}
		return gameDisposeBeanList;
	}
	
	/**
	 * 查询未绑定手机号机器人玩家的剧本信息
	 */
	public List<NotHavePhoneGameBean> getNotHavePhone(){
		List<GameCatalog_Json> gameCatalog_JsonList = JsonCacheManager.getCache(GameCatalogCache.class).getList();
		List<NotHavePhoneGameBean> gameInformation = new ArrayList<NotHavePhoneGameBean>();
		Set<Integer> playerIdAll = new HashSet<Integer>();
		for (GameCatalog_Json gameCatalog_Json : gameCatalog_JsonList) {
			playerIdAll.add(gameCatalog_Json.getAuthorid());
		}
		List<UserEntity> userEntityList = userDao.findAllByPlayerId(playerIdAll);
		for (GameCatalog_Json gameCatalog_Json : gameCatalog_JsonList) {
			boolean flag = true;
			for (UserEntity userEntity : userEntityList) {
				if(Long.valueOf(gameCatalog_Json.getAuthorid()).equals(userEntity.getPlayerId())) {
					flag = false;
					break;
				}
			}
			if(flag) {
				gameInformation.add(new NotHavePhoneGameBean(
						gameCatalog_Json.getAuthorid().toString(),
						gameCatalog_Json.getName(),
						gameCatalog_Json.getAuthorName()));
			}
		}
		return gameInformation;
	}
}
