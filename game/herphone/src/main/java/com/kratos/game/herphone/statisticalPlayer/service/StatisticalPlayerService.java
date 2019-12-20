package com.kratos.game.herphone.statisticalPlayer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.playerOnline.entity.PlayerLoginTimeEntity;
import com.kratos.game.herphone.playerOnline.entity.PlayerOnlineEntity;
import com.kratos.game.herphone.statisticalPlayer.bean.ResStplayerInfo;
import com.kratos.game.herphone.statisticalPlayer.entity.StatisticalPlayerEntity;

@Service
public class StatisticalPlayerService extends BaseService{
	/**获取玩家信息用于护眼大队审核*/
	public List<ResStplayerInfo> listPlayer(int page,int count) {
		List<ResStplayerInfo> list = playerServiceImpl.ListPlayerByAch(page, count);
		for (ResStplayerInfo resStplayerInfo : list) {
			long playerId = Long.parseLong(resStplayerInfo.getPlayerId());
			PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(playerId);
			resStplayerInfo.setNickName(playerDynamicEntity.getNickName());
			resStplayerInfo.setFansCount(playerDynamicEntity.getFansCount());
			resStplayerInfo.setDiscussCount(playerDynamicEntity.getHotDiscussCount());
			PlayerLoginTimeEntity playerLoginTimeEntity = playerLoginTimeDao.findByID(playerId);
			if ( playerLoginTimeEntity == null) {
				resStplayerInfo.setDiscussCount(0);
			}else {
				resStplayerInfo.setTotalLogin(playerLoginTimeEntity.getTotalLogin());
			}
			PlayerOnlineEntity playerOnlineEntity =playerOnlineService.load(playerId);
			resStplayerInfo.setPlayerOnline(playerOnlineEntity.getOnlineTime()/(1000 * 60 * 60));	
		}
		return list;
	}
	/**按时间查询护眼审核列表*/
	public List<ResStplayerInfo> listPlayerByDate(int time,int page,int count) {
		List<StatisticalPlayerEntity> list = statisticalPlayerDao.findlist(page, count, time);
		List<ResStplayerInfo> reslist = new ArrayList<ResStplayerInfo>();
		if (list == null ||list.size() ==0) {
			return reslist;
		}
		for (StatisticalPlayerEntity statisticalPlayerEntity : list) {
			ResStplayerInfo resStplayerInfo = new ResStplayerInfo();
			resStplayerInfo.setPlayerId(String.valueOf(statisticalPlayerEntity.getPlayerId()));
			resStplayerInfo.setRoleId(statisticalPlayerEntity.getRoleId());
			resStplayerInfo.setNickName(statisticalPlayerEntity.getNickName());
			resStplayerInfo.setAchievement(statisticalPlayerEntity.getAchievement());
			resStplayerInfo.setClues(statisticalPlayerEntity.getClues());
			resStplayerInfo.setFansCount(statisticalPlayerEntity.getFansCount());
			resStplayerInfo.setDiscussCount(statisticalPlayerEntity.getDiscussCount());
			resStplayerInfo.setTotalLogin(statisticalPlayerEntity.getTotalLogin());
			resStplayerInfo.setPlayerOnline(statisticalPlayerEntity.getPlayerOnline());
			reslist.add(resStplayerInfo);
		}
		return reslist;
	}
	
	/**
	 * 查询指定时间的护眼大队审核信息总数
    */
	public long assignTimeCount(int time) {
		return statisticalPlayerDao.assignTimeCount(time);
	}
	
	/**
	 * 根据指定的roleId查询玩家信息
	 */
	public List<ResStplayerInfo> findByIdTitleAudit(String roldId,int page,int count) {
		List<StatisticalPlayerEntity> playerList = statisticalPlayerDao.findByIdTitleAudit(roldId,page,count);
		List<ResStplayerInfo> reslist = new ArrayList<ResStplayerInfo>();
		if (playerList == null ||playerList.size() ==0) {
			return reslist;
		}
		for (StatisticalPlayerEntity statisticalPlayerEntity : playerList) {
			ResStplayerInfo resStplayerInfo = new ResStplayerInfo();
			resStplayerInfo.setPlayerId(String.valueOf(statisticalPlayerEntity.getPlayerId()));
			resStplayerInfo.setRoleId(statisticalPlayerEntity.getRoleId());
			resStplayerInfo.setNickName(statisticalPlayerEntity.getNickName());
			resStplayerInfo.setAchievement(statisticalPlayerEntity.getAchievement());
			resStplayerInfo.setClues(statisticalPlayerEntity.getClues());
			resStplayerInfo.setFansCount(statisticalPlayerEntity.getFansCount());
			resStplayerInfo.setDiscussCount(statisticalPlayerEntity.getDiscussCount());
			resStplayerInfo.setTotalLogin(statisticalPlayerEntity.getTotalLogin());
			resStplayerInfo.setPlayerOnline(statisticalPlayerEntity.getPlayerOnline());
			reslist.add(resStplayerInfo);
		}
		return reslist;
	}
	
	/**
	 * 查询指定ID的护眼大队审核列表的数据总数
	 */
	public long findByIdTitleAuditCount(String roldId) {
		return statisticalPlayerDao.findByIdTitleAuditCount(roldId);
	}
	
	/**
	 * 根据ID和时间查询护眼大队玩家审核玩家信息
	 */
	public ResStplayerInfo findByIdAndTimeAudit(String roleId,int date) {
		StatisticalPlayerEntity playerEntity = statisticalPlayerDao.findByRoleIdPlayerId(roleId);
		ResStplayerInfo resStplayerInfo = new ResStplayerInfo();
		if(playerEntity != null) {
			StatisticalPlayerEntity statisticalPlayerEntity = statisticalPlayerDao.findByID(date+"_"+playerEntity.getPlayerId());
			if(statisticalPlayerEntity != null) {
				resStplayerInfo.setPlayerId(String.valueOf(statisticalPlayerEntity.getPlayerId()));
				resStplayerInfo.setRoleId(statisticalPlayerEntity.getRoleId());
				resStplayerInfo.setNickName(statisticalPlayerEntity.getNickName());
				resStplayerInfo.setAchievement(statisticalPlayerEntity.getAchievement());
				resStplayerInfo.setClues(statisticalPlayerEntity.getClues());
				resStplayerInfo.setFansCount(statisticalPlayerEntity.getFansCount());
				resStplayerInfo.setDiscussCount(statisticalPlayerEntity.getDiscussCount());
				resStplayerInfo.setTotalLogin(statisticalPlayerEntity.getTotalLogin());
				resStplayerInfo.setPlayerOnline(statisticalPlayerEntity.getPlayerOnline());
			}
		}
		return resStplayerInfo;
	}
}
