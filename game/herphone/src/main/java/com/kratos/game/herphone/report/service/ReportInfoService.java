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
import com.kratos.game.herphone.common.CommonCost.DiscussType;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.dynamic.entity.DynamicEntity;
import com.kratos.game.herphone.fandom.entity.FandomEntity;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.report.bean.ReqReportBean;
import com.kratos.game.herphone.report.bean.ResReportBean;
import com.kratos.game.herphone.report.entity.ReportInfoEntity;
import com.kratos.game.herphone.util.StringUtil;

@Service
public class ReportInfoService extends BaseService{
	/**
	 * 举报信息
	 * @param reportBean
	 */
	public void reportInfo(ReqReportBean reportBean) {
		long playerId = 0;
		if (reportBean.getType() == DiscussType.script.ordinal()) {
			DiscussEntity discussEntity = discussDao.findByID(reportBean.getDiscussId());
			if (discussEntity == null) {
				throw new BusinessException("参数错误");
			}
			playerId = discussEntity.getFromPlayerId();
		}else if (reportBean.getType() == DiscussType.square.ordinal()) {
			DynamicEntity dynamicEntity = dynamicDao.findByID(reportBean.getDiscussId());
			if (dynamicEntity == null) {
				throw new BusinessException("参数错误");
			}
			playerId = dynamicEntity.getFromPlayerId();
		}else if (reportBean.getType() == DiscussType.fandom.ordinal()) {
			FandomEntity fandomEntity = fandomDao.findByID(reportBean.getDiscussId());
			if (fandomEntity == null) {
				throw new BusinessException("参数错误");
			}
			playerId = fandomEntity.getFromPlayerId();
		}
		Player player = PlayerContext.getPlayer();
		if (playerId == player.getId()) {
			throw new BusinessException("不能举报自己");
		}
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		String dynamicId = reportDynamicId(player.getId(),reportBean.getDiscussId());
		ReportInfoEntity reportInfoEntity = reportInfoDao.findInfoPlayerByDynamicId(dynamicId);
		if (reportInfoEntity != null) {
			throw new BusinessException("不能重复举报");
		}
		reportInfoEntity = new ReportInfoEntity();
		reportInfoEntity.setId(UUID.randomUUID().toString().replace("-", ""));
		reportInfoEntity.setReportInfoDynamicId(dynamicId);
		reportInfoEntity.setDiscussId(reportBean.getDiscussId());
		reportInfoEntity.setType(reportBean.getType());
		reportInfoEntity.setFromPlayerId(player.getId());
		reportInfoEntity.setState(DealState.undeal.ordinal());
		PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(player.getId());
		if (playerDynamicEntity.getItemTitle().contains(gameParams_Json.getProtectEyesTitleId())) {
			reportInfoEntity.setIsTitle(1);
		}else {
			reportInfoEntity.setIsTitle(0);
		}
		reportInfoEntity.setTimeStamp(System.currentTimeMillis());
		reportInfoDao.save(reportInfoEntity);
		reportInfoDistinctService.load(reportBean.getDiscussId(), reportBean.getType(), player.getId());		
	}
	
	private String reportDynamicId(long fromPlayerId,String discussId){
		return StringUtil.appendString(fromPlayerId,discussId);
	}
	//后台管理 举报消息显示
	public List<ResReportBean> listReportInfo(int page,int count,int state) {
		List<ReportInfoEntity> list = reportInfoDao.findTitleList(page, count,state);
		PlayerDynamicEntity playerDynamicEntity =null;
		List<ResReportBean> reslist = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getType() == DiscussType.square.ordinal()) {
				DynamicEntity dynamicEntity = dynamicDao.findByID(list.get(i).getDiscussId());
				playerDynamicEntity = playerDynamicService.load(list.get(i).getFromPlayerId());
				ResReportBean resReportBean = new ResReportBean(list.get(i),
						dynamicEntity,
						playerDynamicEntity,
						simpleDateFormat.format(list.get(i).getTimeStamp()));
				reslist.add(resReportBean);
			}
			if (list.get(i).getType() == DiscussType.script.ordinal()) {				
				DiscussEntity discussEntity = discussDao.findByID(list.get(i).getDiscussId());	
				playerDynamicEntity = playerDynamicService.load(list.get(i).getFromPlayerId());
				ResReportBean resReportBean = new ResReportBean(list.get(i), 
						discussEntity, 
						playerDynamicEntity, 
						simpleDateFormat.format(list.get(i).getTimeStamp()));
				reslist.add(resReportBean);
			} 
			if (list.get(i).getType() == DiscussType.fandom.ordinal()) {				
				FandomEntity fandomEntity = fandomDao.findByID(list.get(i).getDiscussId());	
				playerDynamicEntity = playerDynamicService.load(list.get(i).getFromPlayerId());
				ResReportBean resReportBean = new ResReportBean(list.get(i), 
						fandomEntity, 
						playerDynamicEntity, 
						simpleDateFormat.format(list.get(i).getTimeStamp()));
				reslist.add(resReportBean);
			} 
		}
		if(reslist.size() == 50) {
			return reslist;
		} else {
			int num = 50 - reslist.size();
			List<ReportInfoEntity> reportInfoEntities = reportInfoDao.findLimitList(page, num,state);
			for (int i = 0; i < reportInfoEntities.size(); i++) {
				if (reportInfoEntities.get(i).getType() == DiscussType.square.ordinal()) {
					DynamicEntity dynamicEntity = dynamicDao.findByID(reportInfoEntities.get(i).getDiscussId());
					playerDynamicEntity = playerDynamicService.load(reportInfoEntities.get(i).getFromPlayerId());
					ResReportBean resReportBean = new ResReportBean(reportInfoEntities.get(i),
							dynamicEntity,
							playerDynamicEntity,
							simpleDateFormat.format(reportInfoEntities.get(i).getTimeStamp()));
					reslist.add(resReportBean);
				}
				if (reportInfoEntities.get(i).getType() == DiscussType.script.ordinal()) {				
					DiscussEntity discussEntity = discussDao.findByID(reportInfoEntities.get(i).getDiscussId());	
					playerDynamicEntity = playerDynamicService.load(reportInfoEntities.get(i).getFromPlayerId());
					ResReportBean resReportBean = new ResReportBean(reportInfoEntities.get(i), 
							discussEntity, 
							playerDynamicEntity, 
							simpleDateFormat.format(reportInfoEntities.get(i).getTimeStamp()));
					reslist.add(resReportBean);
				} 
				if (reportInfoEntities.get(i).getType() == DiscussType.fandom.ordinal()) {				
					FandomEntity fandomEntity = fandomDao.findByID(reportInfoEntities.get(i).getDiscussId());	
					playerDynamicEntity = playerDynamicService.load(reportInfoEntities.get(i).getFromPlayerId());
					ResReportBean resReportBean = new ResReportBean(reportInfoEntities.get(i), 
							fandomEntity, 
							playerDynamicEntity, 
							simpleDateFormat.format(reportInfoEntities.get(i).getTimeStamp()));
					reslist.add(resReportBean);
				} 
			}
			return reslist;
		}
	}
	/**取消护眼大队，改变举报信息状态*/
	public void updateIsTitle(long playerId) {
		reportInfoDao.updateByPlayerId(playerId);
	}
	/**
	 * 删除举报信息
	 */
	public void removeReportInfo(String id) {
		ReportInfoEntity reportInfoEntity = reportInfoDao.findByID(id);
		if (reportInfoEntity == null) {
			throw new BusinessException("参数错误");
		}
		reportInfoEntity.setState(DealState.hold.ordinal());
		reportInfoDao.save(reportInfoEntity);
		GMDataChange.recordChange("通过ID删除评论举报信息\t举报ID为",id);
	}
	/**
	 * 举报玩家信息封号处理
	 */
	public void setBlockPlayer(String id,long playerId) {				
		ReportInfoEntity reportInfoEntity = reportInfoDao.findByID(id);
		if (reportInfoEntity == null) {
			return;
		}
		if (playerServiceImpl.get(playerId) == null) {
			return;
		}
		playerServiceImpl.setIsBlock(playerId);
		reportInfoEntity.setState(DealState.deal.ordinal());
		reportInfoDao.save(reportInfoEntity);
		GMDataChange.recordChange("通过ID封禁玩家\t玩家ID为",playerId);
	}
	/**
	 * 举报玩家禁言处理
	 */
	public void setNoSpeak(String id,long playerId,long noSpeakTime) {				
		ReportInfoEntity reportInfoEntity = reportInfoDao.findByID(id);
		if (reportInfoEntity == null) {
			return;
		}
		if (playerServiceImpl.get(playerId) == null) {
			return;
		}
		playerServiceImpl.setNoSpeak(playerId, noSpeakTime);
		reportInfoEntity.setState(DealState.deal.ordinal());
		reportInfoDao.save(reportInfoEntity);
	}
	/**取消被举报人的护眼大队身份*/
	public void removeReportInfoToPlayer(String id,long toPlayerId) {
		ReportInfoEntity reportInfoEntity = reportInfoDao.findByID(id);
		if (reportInfoEntity == null) {
			return;
		}
		playerDynamicService.removeProTitle(toPlayerId);
		reportInfoEntity.setState(DealState.deal.ordinal());
		reportInfoDao.save(reportInfoEntity);
		GMDataChange.recordChange("通过ID移除护眼大队\t玩家ID为",toPlayerId);
	}
	/**
	 * 修改举报玩家状态
	 */
	public void updateState(String id,DealState dealState) {
		ReportInfoEntity reportInfoEntity = reportInfoDao.findByID(id);
		if (reportInfoEntity == null) {
			return;
		}
		reportInfoEntity.setState(dealState.ordinal());
		reportInfoDao.save(reportInfoEntity);
	}
	/**删除评论及处理举报信息*/
	public void dealReportInfo(String id) {
		ReportInfoEntity reportInfoEntity = reportInfoDao.findByID(id);
		if (reportInfoEntity == null) {
			throw new BusinessException("参数错误");
		}
		int type = reportInfoEntity.getType();
		if (type == DiscussType.script.ordinal()) {
			discussService.removeDiscuss(reportInfoEntity.getDiscussId());
		}
		if (type == DiscussType.square.ordinal()) {
			dynamicService.delete(reportInfoEntity.getDiscussId());
		}
		if (type == DiscussType.fandom.ordinal()) {
			fandomService.delete(reportInfoEntity.getDiscussId());
		}
		//将这条评论的所有举报信息清空
		reportInfoDao.removeReportInfoByDiscussId(reportInfoEntity.getDiscussId(), type);
		//护眼先锋审核不在显示
		String reportInfoDistinctId = reportInfoDistinctService.createId(reportInfoEntity.getDiscussId(), type);
		reportInfoDistinctDao.deleteById(reportInfoDistinctId);
		GMDataChange.recordChange("删除举报信息ID为",id);
	}
	/**
	 * 查询举报评论总条数
	 */
	public long getReportDiscussCount() {
		return reportInfoDao.getReportDiscussCount();
	}
}
