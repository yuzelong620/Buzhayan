package com.kratos.game.herphone.report.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.GameParams_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.DealState;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.report.bean.ReqReportBean;
import com.kratos.game.herphone.report.bean.ResReportBean;
import com.kratos.game.herphone.report.entity.ReportInfoEntity;
import com.kratos.game.herphone.report.entity.ReportPlayerEntity;
import com.kratos.game.herphone.util.StringUtil;

@Service
public class ReportPlayerService extends BaseService{
	/**
	 * 举报玩家
	 * @param reportBean
	 */
	public void reportPlayer(ReqReportBean reportBean) {
		Player player = PlayerContext.getPlayer();
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		String dynamicId = reportDynamicId(player.getId(),Long.valueOf(reportBean.getToPlayerId()));
		ReportPlayerEntity reportPlayerEntity = reportPlayerDao.findReportPlayerByDynamicId(dynamicId);
		if (reportPlayerEntity != null) {
			throw new BusinessException("不能重复举报");
		}
		reportPlayerEntity = new ReportPlayerEntity();
		reportPlayerEntity.setId(UUID.randomUUID().toString().replace("-", ""));
		reportPlayerEntity.setReportPlayerDynamicId(dynamicId);
		reportPlayerEntity.setFromPlayerId(player.getId());
		reportPlayerEntity.setToPlayerId(Long.valueOf(reportBean.getToPlayerId()));
		reportPlayerEntity.setContent(reportBean.getContent());
		reportPlayerEntity.setState(DealState.undeal.ordinal());
		PlayerDynamicEntity fromPlayerDynamicEntity = playerDynamicService.load(player.getId());
		PlayerDynamicEntity toPlayerDynamicEntity = playerDynamicService.load(Long.valueOf(reportBean.getToPlayerId()));
		if (fromPlayerDynamicEntity.getItemTitle().contains(gameParams_Json.getProtectEyesTitleId())) {
			reportPlayerEntity.setIsTitle(1);
		}else {
			reportPlayerEntity.setIsTitle(0);
		}
		reportPlayerEntity.setFromPlayerNickName(fromPlayerDynamicEntity.getNickName());
		reportPlayerEntity.setToPlayerNickName(toPlayerDynamicEntity.getNickName());
		reportPlayerEntity.setTimeStamp(System.currentTimeMillis());
		reportPlayerDao.save(reportPlayerEntity);
	}
	private String reportDynamicId(long fromPlayerId,long toPlayerId){
		return StringUtil.appendString(fromPlayerId,toPlayerId);
	}
	//后台管理 举报消息显示
	public List<ResReportBean> listReportPlayer(int page,int count,int state) {
		List<ReportPlayerEntity> list = reportPlayerDao.findTitleList(page, count,state);
		List<ResReportBean> reslist = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < list.size(); i++) {
			ResReportBean resReportBean = new ResReportBean();
			resReportBean.setId(list.get(i).getId());
			resReportBean.setToplayerId(String.valueOf(list.get(i).getToPlayerId()));
			resReportBean.setToplayer(list.get(i).getToPlayerNickName());
			resReportBean.setContent(list.get(i).getContent());
			resReportBean.setFromplayer(list.get(i).getFromPlayerNickName());
			resReportBean.setProtectEyeTitle(list.get(i).getIsTitle());
			resReportBean.setTimestamp(simpleDateFormat.format(list.get(i).getTimeStamp()));
			reslist.add(resReportBean);
		}
		if(reslist.size() == 50) {
			return reslist;
		} else {
			int num = 50 - reslist.size();
			List<ReportPlayerEntity> reportPlayerEntities = reportPlayerDao.findLimitList(page, num,state);
			for(int i = 0; i < reportPlayerEntities.size();i++) {
				ResReportBean resReportBean = new ResReportBean();
				resReportBean.setId(reportPlayerEntities.get(i).getId());
				resReportBean.setToplayerId(String.valueOf(reportPlayerEntities.get(i).getToPlayerId()));
				resReportBean.setToplayer(reportPlayerEntities.get(i).getToPlayerNickName());
				resReportBean.setContent(reportPlayerEntities.get(i).getContent());
				resReportBean.setFromplayer(reportPlayerEntities.get(i).getFromPlayerNickName());
				resReportBean.setProtectEyeTitle(reportPlayerEntities.get(i).getIsTitle());
				resReportBean.setTimestamp(simpleDateFormat.format(reportPlayerEntities.get(i).getTimeStamp()));
				reslist.add(resReportBean);
			}
			return reslist;
		}
	}
		/**
		 * 删除举报信息
		 */
		public void removeReportPlayer(String id) {
			GMDataChange.recordChange("通过ID删除举报玩家信息\t举报ID为",id);
			reportPlayerDao.deleteById(id);
		}
		/**
		 * 举报玩家封号处理
		 */
		public void setBlockPlayer(String id,long playerId) {				
			ReportPlayerEntity reportPlayerEntity = reportPlayerDao.findByID(id);
			if (reportPlayerEntity == null) {
				return;
			}
			if (playerServiceImpl.get(playerId) == null) {
				return;
			}
			playerServiceImpl.setIsBlock(playerId);
			reportPlayerEntity.setState(DealState.deal.ordinal());
			reportPlayerDao.save(reportPlayerEntity);
			GMDataChange.recordChange("通过ID封禁玩家\t玩家ID为",playerId);
		}
		/**
		 * 举报玩家禁言处理
		 */
		public void setNoSpeak(String id,long playerId,long noSpeakTime) {				
			ReportPlayerEntity reportPlayerEntity = reportPlayerDao.findByID(id);
			if (reportPlayerEntity == null) {
				return;
			}
			if (playerServiceImpl.get(playerId) == null) {
				return;
			}
			playerServiceImpl.setNoSpeak(playerId, noSpeakTime);
			reportPlayerEntity.setState(DealState.deal.ordinal());
			reportPlayerDao.save(reportPlayerEntity);
			GMDataChange.recordChange("禁言ID为" + playerId + "\t禁言时长(毫秒)",noSpeakTime);
		}
		
		/**取消护眼大队，改变举报信息状态*/
		public void updateIsTitle(long playerId) {
			reportPlayerDao.updateByPlayerId(playerId);
		}
		
		/**取消被举报人的护眼大队身份*/
		public void removeReportPlayerToPlayer(String id,long toPlayerId) {
			ReportPlayerEntity reportPlayerEntity = reportPlayerDao.findByID(id);
			if (reportPlayerEntity == null) {
				return;
			}
			playerDynamicService.removeProTitle(toPlayerId);
			reportPlayerEntity.setState(DealState.deal.ordinal());
			reportPlayerDao.save(reportPlayerEntity);
			GMDataChange.recordChange("通过ID移除护眼大队\t玩家ID为",toPlayerId);
		}
		/**
		 * 修改举报玩家状态
		 */
		public void updateState(String id,DealState dealState) {
			ReportPlayerEntity reportPlayerEntity = reportPlayerDao.findByID(id);
			if (reportPlayerEntity == null) {
				return;
			}
			reportPlayerEntity.setState(dealState.ordinal());
			reportPlayerDao.save(reportPlayerEntity);
		}
		
		/**
		 * 查询举报玩家信息总条数
		 */
		public long getReportPlayerCount() {
			return reportPlayerDao.getReportPlayerCount();
		}
		
}
