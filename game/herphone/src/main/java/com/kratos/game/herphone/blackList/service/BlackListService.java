package com.kratos.game.herphone.blackList.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.GameParams_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.blackList.bean.ResBlackPlayer;
import com.kratos.game.herphone.blackList.entity.BlackListEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

@Service
public class BlackListService extends BaseService{
	/**
	 * load方法
	 * @return
	 */
	public BlackListEntity load() {
		Player player = PlayerContext.getPlayer();
		BlackListEntity blackListEntity = blackListDao.findByID(player.getId());
		if (blackListEntity == null) {
			blackListEntity = new BlackListEntity();
			blackListEntity.setPlayerId(player.getId());
			blackListDao.save(blackListEntity);
		}		
		return blackListEntity;
	}
	/**
	 * load方法
	 * @return
	 */
	public BlackListEntity load(long playerId) {
		BlackListEntity blackListEntity = blackListDao.findByID(playerId);
		if (blackListEntity == null) {
			blackListEntity = new BlackListEntity();
			blackListEntity.setPlayerId(playerId);
			blackListDao.save(blackListEntity);
		}		
		return blackListEntity;
	}
	/**
	 * 添加玩家到黑名单
	 * @param playerId
	 */
	public ResBlackPlayer defriend(long playerId) {
		BlackListEntity blackListEntity = load();
		if (blackListEntity.getPlayerId() == playerId) {
			throw new BusinessException("不能拉黑自己");
		}
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		
		if (playerServiceImpl.get(playerId) == null) {
			throw new BusinessException("参数错误");
		}
		if (blackListEntity.getBlackList().size() > gameParams_Json.getBlackListMax()) {
			throw new BusinessException("您的黑名单名额好像满了哦~");
		}
		if (blackListEntity.getBlackList().contains(playerId)) {
			throw new BusinessException("不能重复拉黑");
		}
		blackListDao.addBlack(blackListEntity.getPlayerId(), playerId);
		String att1 = attentionService.createId(playerId, blackListEntity.getPlayerId()); //我关注他
		String att2 = attentionService.createId(blackListEntity.getPlayerId(), playerId); //他关注我
		if (attentionDao.findByID(att1)!= null) {
			attentionDao.deleteById(att1);
			playerDynamicDao.incAttentionNum(blackListEntity.getPlayerId(),-1);//我关注数 -1
			playerDynamicDao.incFansNum(playerId,-1);//对方 粉丝数-1
		}
		if (attentionDao.findByID(att2)!=null) {
			attentionDao.deleteById(att2);
			playerDynamicDao.incAttentionNum(playerId,-1);//对方关注数 -1
			playerDynamicDao.incFansNum(blackListEntity.getPlayerId(),-1);// 我粉丝数-1
		};
		PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(playerId);
		ResBlackPlayer resBlackPlayer = new ResBlackPlayer();
		resBlackPlayer.setPlayerId(String.valueOf(playerId));
		resBlackPlayer.setHeadImgUrl(playerDynamicEntity.getHeadImgUrl());
		resBlackPlayer.setNickName(playerDynamicEntity.getNickName());
		resBlackPlayer.setSignature(playerDynamicEntity.getSignature());
		return resBlackPlayer;
	}
	/**
	 * 从黑名单移除玩家
	 */
	public void removeBlackList(long playerId) {
		BlackListEntity blackListEntity = load();
		if (playerServiceImpl.get(playerId) == null) {
			throw new BusinessException("参数错误");
		}
		if (!blackListEntity.getBlackList().contains(playerId)) {
			return;
		}
		blackListDao.removeBlack(blackListEntity.getPlayerId(), playerId);
	}
	public List<ResBlackPlayer> getBlackList() {
		BlackListEntity blackListEntity = load();
		List<Long> playerIds = blackListEntity.getBlackList();
		List<ResBlackPlayer> reslist = new ArrayList<>();
		if (playerIds == null||playerIds.size()==0) {
			return reslist;
		}
	
		for (int i = 0; i < playerIds.size(); i++) {
			PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(playerIds.get(i));	
			ResBlackPlayer resBlackPlayer = new ResBlackPlayer();
			resBlackPlayer.setPlayerId(playerIds.get(i).toString());
			resBlackPlayer.setHeadImgUrl(playerDynamicEntity.getHeadImgUrl());
			resBlackPlayer.setNickName(playerDynamicEntity.getNickName());
			resBlackPlayer.setSignature(playerDynamicEntity.getSignature());
			reslist.add(resBlackPlayer);
		}
		return reslist;
		
	}
}
