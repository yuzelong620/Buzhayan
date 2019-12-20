package com.kratos.game.herphone.pioneer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.GameParams_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.pioneer.bean.ResPioneerBean;
import com.kratos.game.herphone.pioneer.entity.PioneerEntity;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.user.entity.UserEntity;

@Service
public class PioneerService extends BaseService{
	
	public PioneerEntity load(long playerId) {
		PioneerEntity pioneerEntity = pioneerDao.findByID(playerId);
		if (pioneerEntity == null) {
			pioneerEntity = new PioneerEntity();
			pioneerEntity.setPlayerId(playerId);
			pioneerEntity.setCreatTime(System.currentTimeMillis());
			pioneerDao.save(pioneerEntity);
		}
		return pioneerEntity;
	}
	/**获取分配举报信息数最少的护眼先锋Id*/
	public List<Long> listTopThreePlayer() {
		List<PioneerEntity> list = pioneerDao.listTopThreePlayer();
		List<Long> reslist = new ArrayList<Long>();
		if (list == null||list.size()<3) {
			return reslist;
		}
		for (PioneerEntity pioneerEntity : list) {
			reslist.add(pioneerEntity.getPlayerId());
		}
		return reslist;
		
	}
	/**获取护眼先锋列表*/
	public List<ResPioneerBean> listPioneer(int page,int count) {
		List<PioneerEntity> list = pioneerDao.listPioneer(page,count);
		List<ResPioneerBean> reslist = new ArrayList<>();
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		for (PioneerEntity pioneerEntity : list) {
			ResPioneerBean resPioneerBean = new ResPioneerBean();
			PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(pioneerEntity.getPlayerId());
			resPioneerBean.setPlayerId(String.valueOf(pioneerEntity.getPlayerId()));
			resPioneerBean.setCreatTime(pioneerEntity.getCreatTime());
			resPioneerBean.setDealNum(pioneerEntity.getDealNum());
			resPioneerBean.setSucccessNum(pioneerEntity.getSuccessNum());
			resPioneerBean.setRoleId(playerDynamicEntity.getRoleId());
			resPioneerBean.setNickName(playerDynamicEntity.getNickName());
			resPioneerBean.setSex(playerDynamicEntity.getSex());
			if (playerDynamicEntity.getItemTitle().contains(gameParams_Json.getProtectEyesTitleId())) {
				resPioneerBean.setIsEyeshield(1);
			}else {
				resPioneerBean.setIsEyeshield(0);
			}
			reslist.add(resPioneerBean);
		}
		return reslist;
	}
	/**解除护眼先锋*/
	public void removePioneer(long playerId) {
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		PioneerEntity pioneerEntity = pioneerDao.findByID(playerId);
		if (pioneerEntity == null) {
			throw new BusinessException("参数错误");
		}
		pioneerDao.deleteById(playerId);
		playerDynamicDao.removeTitle(gameParams_Json.getPioneerId(), playerId);
		GMDataChange.recordChange("解除护眼先锋\t护眼先锋ID为",playerId);
	}
	/**根据角色id设置护眼先锋*/
	public void addPioneerByRoleId(String roleId) {
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		Long playerId = playerServiceImpl.getPlayerByRoleId(roleId);
		if (playerId == null) {
			throw new BusinessException("未找到玩家");
		}
		PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(playerId);
		List<Integer> list = playerDynamicEntity.getItemTitle();
		if (list.contains(gameParams_Json.getPioneerId())) {
			throw new BusinessException("该玩家已经是护眼先锋");
		}	
		PioneerEntity pioneerEntity = new PioneerEntity();
		pioneerEntity.setPlayerId(playerId);
		pioneerEntity.setCreatTime(System.currentTimeMillis());
		pioneerDao.save(pioneerEntity);
		playerDynamicDao.addToSetItemTitle(playerDynamicEntity.getPlayerId(), gameParams_Json.getPioneerId());
		commonService.resetPlayerExtra(playerService.get(playerDynamicEntity.getPlayerId()), playerDynamicEntity);
		GMDataChange.recordChange("通过roleId设置护眼先锋\troleId为",roleId);
	}
	/**根据手机设置护眼先锋*/
	public void addPioneerByPhone(String phone) {
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		UserEntity userEntity = userDao.findDataByPhone(phone);
		if(userEntity == null) {
			throw new BusinessException("未找到玩家");
		}
		PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(userEntity.getPlayerId());
		List<Integer> list = playerDynamicEntity.getItemTitle();
		if (list.contains(gameParams_Json.getPioneerId())) {
			throw new BusinessException("该玩家已经是护眼先锋");
		}	
		PioneerEntity pioneerEntity = new PioneerEntity();
		pioneerEntity.setPlayerId(userEntity.getPlayerId());
		pioneerEntity.setCreatTime(System.currentTimeMillis());
		pioneerDao.save(pioneerEntity);
		playerDynamicDao.addToSetItemTitle(playerDynamicEntity.getPlayerId(), gameParams_Json.getPioneerId());
		commonService.resetPlayerExtra(playerService.get(playerDynamicEntity.getPlayerId()), playerDynamicEntity);
		GMDataChange.recordChange("通过手机号设置护眼先锋\t手机号为",phone);
	}
	/**根据roleId获取护眼先锋*/
	public ResPioneerBean getPioneerByRoleId(String roleId) {
		Long playerId = playerServiceImpl.getPlayerByRoleId(roleId);
		if (playerId == null) {
			throw new BusinessException("未找到玩家");
		}
		PioneerEntity pioneerEntity = pioneerDao.findByID(playerId);
		if (pioneerEntity == null) {
			throw new BusinessException("该玩家不是护眼先锋");
		}
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		ResPioneerBean resPioneerBean = new ResPioneerBean();
		PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(pioneerEntity.getPlayerId());
		resPioneerBean.setPlayerId(String.valueOf(pioneerEntity.getPlayerId()));
		resPioneerBean.setCreatTime(pioneerEntity.getCreatTime());
		resPioneerBean.setDealNum(pioneerEntity.getDealNum());
		resPioneerBean.setSucccessNum(pioneerEntity.getSuccessNum());
		resPioneerBean.setRoleId(playerDynamicEntity.getRoleId());
		resPioneerBean.setNickName(playerDynamicEntity.getNickName());
		resPioneerBean.setSex(playerDynamicEntity.getSex());
		if (playerDynamicEntity.getItemTitle().contains(gameParams_Json.getProtectEyesTitleId())) {
			resPioneerBean.setIsEyeshield(1);
		}else {
			resPioneerBean.setIsEyeshield(0);
		}
		return resPioneerBean;
	}
	/**
	 * 查询护眼先锋总人数
	 */
	public long pioneerCount() {
		return pioneerDao.pioneerCount();
	}
}
