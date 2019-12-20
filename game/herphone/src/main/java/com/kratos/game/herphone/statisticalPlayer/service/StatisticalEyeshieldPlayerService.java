package com.kratos.game.herphone.statisticalPlayer.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.playerDynamic.bean.ResItemTitleBean;
import com.kratos.game.herphone.statisticalPlayer.entity.StatisticalEyeshieldPlayerEntity;


@Service
public class StatisticalEyeshieldPlayerService extends BaseService{
	/**按时间查询护眼大队列表*/
	public List<ResItemTitleBean> listPlayerByDate(int time,int page,int count) {
		List<StatisticalEyeshieldPlayerEntity> list = statisticalEyeshieldPlayerDao.findlist(page, count, time);
		List<ResItemTitleBean> reslist = new ArrayList<ResItemTitleBean>();
		if (list == null ||list.size() ==0) {
			return reslist;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (StatisticalEyeshieldPlayerEntity statisticalEyeshieldPlayerEntity : list) {
			ResItemTitleBean resItemTitleBean = new ResItemTitleBean();
			resItemTitleBean.setPlayerId(String.valueOf(statisticalEyeshieldPlayerEntity.getPlayerId()));
			resItemTitleBean.setRoleId(statisticalEyeshieldPlayerEntity.getRoleId());
			resItemTitleBean.setSetTitleTime(simpleDateFormat.format(statisticalEyeshieldPlayerEntity.getSetTitleTime()));
			resItemTitleBean.setNickName(statisticalEyeshieldPlayerEntity.getNickName());
			resItemTitleBean.setSex(statisticalEyeshieldPlayerEntity.getSex());
			resItemTitleBean.setFansCount(statisticalEyeshieldPlayerEntity.getFansCount());
			resItemTitleBean.setSendDiscussNum(statisticalEyeshieldPlayerEntity.getSendDiscussNum());
			resItemTitleBean.setSendGodDiscuss(statisticalEyeshieldPlayerEntity.getSendGodDiscuss());
			resItemTitleBean.setTotalLogin(statisticalEyeshieldPlayerEntity.getTotalLogin());
			resItemTitleBean.setOnline(statisticalEyeshieldPlayerEntity.getOnline());
			reslist.add(resItemTitleBean);
		}
		return reslist;
	}
	
	/**
	 * 查询指定时间的护眼大队信息总数
	 */
	public long assignEyeTimeCount(int time) {
		return statisticalEyeshieldPlayerDao.assignEyeTimeCount(time);
	}

	/**
	 * 指定ID查询护眼大队玩家信息变化
	 */
	public List<ResItemTitleBean> getByIdAuditPlayer(String roleId, int page, int count) {
		List<StatisticalEyeshieldPlayerEntity> list = statisticalEyeshieldPlayerDao.findByRoleIdList(page, count, roleId);
		List<ResItemTitleBean> reslist = new ArrayList<ResItemTitleBean>();
		if (list == null ||list.size() ==0) {
			return reslist;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (StatisticalEyeshieldPlayerEntity statisticalEyeshieldPlayerEntity : list) {
			ResItemTitleBean resItemTitleBean = new ResItemTitleBean();
			resItemTitleBean.setPlayerId(String.valueOf(statisticalEyeshieldPlayerEntity.getPlayerId()));
			resItemTitleBean.setRoleId(statisticalEyeshieldPlayerEntity.getRoleId());
			resItemTitleBean.setSetTitleTime(simpleDateFormat.format(statisticalEyeshieldPlayerEntity.getSetTitleTime()));
			resItemTitleBean.setNickName(statisticalEyeshieldPlayerEntity.getNickName());
			resItemTitleBean.setSex(statisticalEyeshieldPlayerEntity.getSex());
			resItemTitleBean.setFansCount(statisticalEyeshieldPlayerEntity.getFansCount());
			resItemTitleBean.setSendDiscussNum(statisticalEyeshieldPlayerEntity.getSendDiscussNum());
			resItemTitleBean.setSendGodDiscuss(statisticalEyeshieldPlayerEntity.getSendGodDiscuss());
			resItemTitleBean.setTotalLogin(statisticalEyeshieldPlayerEntity.getTotalLogin());
			resItemTitleBean.setOnline(statisticalEyeshieldPlayerEntity.getOnline());
			reslist.add(resItemTitleBean);
		}
		return reslist;
	}

	/**
	 * 指定ID查询护眼大队玩家信息变化总数
	 */
	public long getByIdAuditPlayerCount(String roleId) {
		return statisticalEyeshieldPlayerDao.getByIdAuditPlayerCount(roleId);
	}

	/**
	 * 根据ID和时间查询护眼大队玩家信息
	 */
	public ResItemTitleBean findByIdAndTimeTitle(String roleId, int date) {
		StatisticalEyeshieldPlayerEntity playerEntity = statisticalEyeshieldPlayerDao.findByRoleIdPlayerId(roleId);
		ResItemTitleBean resItemTitleBean = new ResItemTitleBean();
		if(playerEntity != null) {
			StatisticalEyeshieldPlayerEntity statisticalEyeshieldPlayerEntity = statisticalEyeshieldPlayerDao.findByID(date+"_"+playerEntity.getPlayerId());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(statisticalEyeshieldPlayerEntity != null) {
				resItemTitleBean.setPlayerId(String.valueOf(statisticalEyeshieldPlayerEntity.getPlayerId()));
				resItemTitleBean.setRoleId(statisticalEyeshieldPlayerEntity.getRoleId());
				resItemTitleBean.setSetTitleTime(simpleDateFormat.format(statisticalEyeshieldPlayerEntity.getSetTitleTime()));
				resItemTitleBean.setNickName(statisticalEyeshieldPlayerEntity.getNickName());
				resItemTitleBean.setSex(statisticalEyeshieldPlayerEntity.getSex());
				resItemTitleBean.setFansCount(statisticalEyeshieldPlayerEntity.getFansCount());
				resItemTitleBean.setSendDiscussNum(statisticalEyeshieldPlayerEntity.getSendDiscussNum());
				resItemTitleBean.setSendGodDiscuss(statisticalEyeshieldPlayerEntity.getSendGodDiscuss());
				resItemTitleBean.setTotalLogin(statisticalEyeshieldPlayerEntity.getTotalLogin());
				resItemTitleBean.setOnline(statisticalEyeshieldPlayerEntity.getOnline());
			}
		}
		return resItemTitleBean;
	}
}
