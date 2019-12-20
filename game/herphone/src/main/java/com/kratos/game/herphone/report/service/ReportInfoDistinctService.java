package com.kratos.game.herphone.report.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.DealState;
import com.kratos.game.herphone.common.CommonCost.DiscussType;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.dynamic.bean.DynamicBean;
import com.kratos.game.herphone.dynamic.entity.DynamicEntity;
import com.kratos.game.herphone.fandom.bean.FandomBean;
import com.kratos.game.herphone.fandom.entity.FandomEntity;
import com.kratos.game.herphone.message.bean.DiscussBean;
import com.kratos.game.herphone.pioneer.entity.PioneerEntity;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.report.bean.ReqDealReportInfoDistinct;
import com.kratos.game.herphone.report.bean.ResReportInfoDistinct;
import com.kratos.game.herphone.report.bean.ResReportInfoDistinctBean;
import com.kratos.game.herphone.report.entity.ReportInfoDistinctEntity;
@Service
public class ReportInfoDistinctService extends BaseService{
	
	public ReportInfoDistinctEntity load(String discussId,int type,long playerId) {
		String id = createId(discussId, type);
		ReportInfoDistinctEntity reportInfoDistinctEntity = reportInfoDistinctDao.findByID(id);
		if (reportInfoDistinctEntity == null) {
			List<Long> list = pioneerService.listTopThreePlayer();
			reportInfoDistinctEntity = new ReportInfoDistinctEntity();
			if (list.size() == 0) {
				return null;
			}
			reportInfoDistinctEntity.setId(id);
			reportInfoDistinctEntity.setDiscussId(discussId);
			reportInfoDistinctEntity.setType(type);
			reportInfoDistinctEntity.setReportPlayerId(playerId);
			reportInfoDistinctEntity.getPushPlayers().addAll(list);
			reportInfoDistinctEntity.setDealOpinion(0);
			reportInfoDistinctEntity.setVersion(0);
			reportInfoDistinctEntity.setReportTime(System.currentTimeMillis());
			reportInfoDistinctDao.save(reportInfoDistinctEntity);
		}
		return reportInfoDistinctEntity;
	}
	public String createId(String discussId,int type){
		 return new StringBuffer().append(discussId).append("_").append(type).toString();
	  }
	/**护眼先锋获取举报评论列表*/
	public Object listReportInfoDistinctByType(int page,int type) {
		Player player = PlayerContext.getPlayer();
		PioneerEntity pioneerEntity = pioneerDao.findByID(player.getId());
		if (pioneerEntity == null) {
			throw new BusinessException("你还不是护眼先锋成员");
		}
		int count = 30;
		List<ReportInfoDistinctEntity> list = reportInfoDistinctDao.listReportInfoDistinctByType(player.getId(), count, page, type,DealState.undeal.ordinal());
		List<String> discussIds = new ArrayList<>();
		for (ReportInfoDistinctEntity reportInfoDistinctEntity : list) {
			discussIds.add(reportInfoDistinctEntity.getDiscussId());
		}
		Object object = swichByType(type,discussIds);
		ResReportInfoDistinctBean reportInfoDistinctBean = new ResReportInfoDistinctBean();
		reportInfoDistinctBean.setPage(page);
		reportInfoDistinctBean.setList(object);
		return reportInfoDistinctBean;
	}
	public Object swichByType(int type,List<String> list) {
		if (type == DiscussType.script.ordinal()) {
			List<DiscussBean> reslist = new ArrayList<DiscussBean>();
			List<DiscussEntity> discussEntitys = discussDao.findByIds(list);
			for (DiscussEntity discussEntity : discussEntitys) {
				reslist.add(new DiscussBean(discussEntity));
			}
			return reslist;
		}
		if (type == DiscussType.square.ordinal()) {
			List<DynamicBean> dynamicBeans=new ArrayList<>();
			List<DynamicEntity> dynamicEntitys = dynamicDao.findByIds(list);
			for (DynamicEntity dynamicEntity : dynamicEntitys) {
				dynamicBeans.add(new DynamicBean(dynamicEntity));
			}
			return dynamicBeans;
		}
		if (type == DiscussType.fandom.ordinal()) {
			List<FandomBean> arr=new ArrayList<FandomBean>();
			List<FandomEntity> fandomEntitys = fandomDao.findByIds(list);
			for (FandomEntity fandomEntity : fandomEntitys) {
				arr.add(new FandomBean(fandomEntity));
			}
			return arr;
		}
		return null;
	}
	
	/**护眼先锋处理举报信息*/
	public void dealReportInfoDistinct(ReqDealReportInfoDistinct reportInfoDistinct) {
		String id = createId(reportInfoDistinct.getDiscussid(), reportInfoDistinct.getType());
		ReportInfoDistinctEntity reportInfoDistinctEntity = reportInfoDistinctDao.findByID(id);
		if (reportInfoDistinctEntity == null) {
			throw new BusinessException("参数错误");
		}
		Player player = PlayerContext.getPlayer();
		PioneerEntity pioneerEntity = pioneerDao.findByID(player.getId());
		if (pioneerEntity ==null) {
			throw new BusinessException("该玩家不是护眼先锋");
		}
		//0不处理1处理
		switch (reportInfoDistinct.getOpinion()) {
		//忽略举报信息
		case 0:
			boolean b = reportInfoDistinctDao.ignoreReportInfoDistinct(player.getId(), id, reportInfoDistinctEntity.getVersion());
			while (!b) {
				reportInfoDistinctEntity = reportInfoDistinctDao.findByID(id);
				b = reportInfoDistinctDao.ignoreReportInfoDistinct(player.getId(), id, reportInfoDistinctEntity.getVersion());			
			}
			pioneerDao.incDealNum(player.getId(), 1);
			break;
		//处理举报信息
		case 1:
			if (reportInfoDistinctEntity.getDealPlayers().size() > 0) {
				boolean boo = reportInfoDistinctDao.pushReportInfoDistinct(player.getId(), id, reportInfoDistinctEntity.getVersion(),DealState.deal.ordinal());
				while (!boo) {
					reportInfoDistinctEntity = reportInfoDistinctDao.findByID(id);
					boo = reportInfoDistinctDao.pushReportInfoDistinct(player.getId(), id, reportInfoDistinctEntity.getVersion(),DealState.deal.ordinal());				
				}
				pioneerDao.incDealNum(player.getId(), 1);
			}else {
				boolean boo = reportInfoDistinctDao.pushReportInfoDistinct(player.getId(), id, reportInfoDistinctEntity.getVersion(),DealState.undeal.ordinal());
				//处理举报信息
				while (!boo) {		
					reportInfoDistinctEntity = reportInfoDistinctDao.findByID(id);
					boo = reportInfoDistinctDao.pushReportInfoDistinct(player.getId(), id, reportInfoDistinctEntity.getVersion(),DealState.undeal.ordinal());				
				}		
				pioneerDao.incDealNum(player.getId(), 1);
			}
			break;
		default:
			throw new BusinessException("选项参数错误");
		}
	}
	
	/**后台护眼先锋处理评论管理*/
	public List<ResReportInfoDistinct> listResReportInfoDistinct(int page) {
		int count= 10;
		List<ReportInfoDistinctEntity> list = reportInfoDistinctDao.listReportInfo(page,count,DealState.deal.ordinal());
		Set<Long> set = new HashSet<>();
		List<ResReportInfoDistinct> reslist = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (ReportInfoDistinctEntity reportInfoDistinctEntity : list) {
			ResReportInfoDistinct resReportInfoDistinct = new ResReportInfoDistinct();
			resReportInfoDistinct.setId(reportInfoDistinctEntity.getId());
			resReportInfoDistinct.setDiscussId(reportInfoDistinctEntity.getDiscussId());
			resReportInfoDistinct.setType(reportInfoDistinctEntity.getType());
			resReportInfoDistinct.setReportPlayerId(reportInfoDistinctEntity.getReportPlayerId());
			resReportInfoDistinct.setDealPlayer1Id(reportInfoDistinctEntity.getDealPlayers().get(0));
			resReportInfoDistinct.setDealPlayer2Id(reportInfoDistinctEntity.getDealPlayers().get(1));
			resReportInfoDistinct.setCreatTime(simpleDateFormat.format(reportInfoDistinctEntity.getReportTime()));
			set.add(reportInfoDistinctEntity.getReportPlayerId());
			set.add(reportInfoDistinctEntity.getDealPlayers().get(0));
			set.add(reportInfoDistinctEntity.getDealPlayers().get(1));
			reslist.add(resReportInfoDistinct);
		}
		Map<Long, String> map = new HashMap<>();
		List<PlayerDynamicEntity> playerDynamicEntities = playerDynamicDao.findByIds(set);
		for (PlayerDynamicEntity playerDynamicEntity : playerDynamicEntities) {
			map.put(playerDynamicEntity.getPlayerId(), playerDynamicEntity.getNickName());
		}
		for (ResReportInfoDistinct resReportInfoDistinct : reslist) {
			//评论的类型 0剧本1广场2粉圈
			switch (resReportInfoDistinct.getType()) {
			case 0:
				resReportInfoDistinct.setContent(discussDao.findByID(resReportInfoDistinct.getDiscussId()).getContent());
				break;
			case 1:
				resReportInfoDistinct.setContent(JSON.toJSONString(dynamicDao.findByID(resReportInfoDistinct.getDiscussId()).getResList()));
				break;
			case 2:
				resReportInfoDistinct.setContent(fandomDao.findByID(resReportInfoDistinct.getDiscussId()).getMessage());
				break;
			default:
				break;
			}			
			resReportInfoDistinct.setDealPlayer1(map.get(resReportInfoDistinct.getDealPlayer1Id()));
			resReportInfoDistinct.setDealPlayer2(map.get(resReportInfoDistinct.getDealPlayer2Id()));
			resReportInfoDistinct.setReportPlayer(map.get(resReportInfoDistinct.getReportPlayerId()));
		}
		return reslist;
	}
	/**删除护眼先锋审核 并且删除评论*/
	public void removeDiscuss(String id) {
		ReportInfoDistinctEntity reportInfoDistinctEntity = reportInfoDistinctDao.findByID(id);
		if (reportInfoDistinctEntity == null) {
			throw new BusinessException("参数错误");
		}
		if (reportInfoDistinctEntity.getDealPlayers().size() < 1) {
			throw new BusinessException("处理玩家数量不足");
		}
		long now = System.currentTimeMillis();
		switch (reportInfoDistinctEntity.getType()) {
		//评论的类型 0剧本1广场2粉圈
		case 0:
			discussDao.removeByIdDiscuss(reportInfoDistinctEntity.getDiscussId(),now);
			reportInfoDao.removeReportInfoByDiscussId(reportInfoDistinctEntity.getDiscussId(),reportInfoDistinctEntity.getType());
			reportInfoDistinctDao.deleteById(id);
			pioneerDao.incDealNum(reportInfoDistinctEntity.getDealPlayers().get(0), 1);
			pioneerDao.incDealNum(reportInfoDistinctEntity.getDealPlayers().get(1), 1);
			break;
		case 1:
			dynamicDao.setIsDelete(reportInfoDistinctEntity.getDiscussId());
			reportInfoDistinctDao.deleteById(id);
			reportInfoDao.removeReportInfoByDiscussId(reportInfoDistinctEntity.getDiscussId(),reportInfoDistinctEntity.getType());
			pioneerDao.incDealNum(reportInfoDistinctEntity.getDealPlayers().get(0), 1);
			pioneerDao.incDealNum(reportInfoDistinctEntity.getDealPlayers().get(1), 1);
			break;
		case 2:
			fandomDao.delete(reportInfoDistinctEntity.getDiscussId());
			reportInfoDistinctDao.deleteById(id);
			reportInfoDao.removeReportInfoByDiscussId(reportInfoDistinctEntity.getDiscussId(),reportInfoDistinctEntity.getType());
			pioneerDao.incDealNum(reportInfoDistinctEntity.getDealPlayers().get(0), 1);
			pioneerDao.incDealNum(reportInfoDistinctEntity.getDealPlayers().get(1), 1);
			break;
		default:
			break;
		}	
		GMDataChange.recordChange("删除护眼先锋审核信息\t审核ID为",id);
	}
	/**获取被举报人信息*/
	public PlayerDynamicEntity getReportPlayer(String discussId,int type) {
	//评论的类型 0剧本1广场2粉圈
		long playerId =0;
		switch (type) {
		case 0:
			DiscussEntity discussEntity = discussDao.findByID(discussId);
			if (discussEntity == null) {
				throw new BusinessException("参数错误");
			}
			playerId = discussEntity.getFromPlayerId();
			break;
		case 1:
			DynamicEntity dynamicEntity = dynamicDao.findByID(discussId);
			if (dynamicEntity == null) {
				throw new BusinessException("参数错误");
			}
			playerId = dynamicEntity.getFromPlayerId();
			break;
		case 2:
			FandomEntity fandomEntity = fandomDao.findByID(discussId);
			if (fandomEntity == null) {
				throw new BusinessException("参数错误");
			}
			playerId = fandomEntity.getFromPlayerId();
			break;
		default:
			break;
		}
		PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(playerId);
		return playerDynamicEntity;
	}
	
	/**
	 * 查询护眼先锋举报信息总数
	 */
	public long pioneerReportCount() {
		return reportInfoDistinctDao.pioneerReportCount();
	}
}
