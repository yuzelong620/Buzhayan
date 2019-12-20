package com.kratos.game.herphone.statisticalPlayer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.statisticalPlayer.bean.ResStatisticalPioneer;
import com.kratos.game.herphone.statisticalPlayer.entity.StatisticalPioneerEntity;

@Service
public class StatisticalPioneerService extends BaseService{
	/**根据时间查询列表*/
	public List<ResStatisticalPioneer> listStatisticalPioneerBytime(int date,int page,int count) {
		List<StatisticalPioneerEntity> list = statisticalPioneerDao.findlist(page, count, date);
		List<ResStatisticalPioneer> reslist = new ArrayList<>();
		if (list == null || list.size()  == 0) {
			return reslist;
		}
		for (StatisticalPioneerEntity statisticalPioneerEntity : list) {
			ResStatisticalPioneer resStatisticalPioneer = new ResStatisticalPioneer(statisticalPioneerEntity);
			reslist.add(resStatisticalPioneer);
		}
		return reslist;
	}
	/**根据时间和roleId查询*/
	public ResStatisticalPioneer getStatisticalPioneer(String roleId,int date) {
		Long playerId = playerServiceImpl.getPlayerByRoleId(roleId);
		if (playerId == null) {
			throw new BusinessException("未找到玩家");
		}
		String id = createId(playerId, date);
		StatisticalPioneerEntity statisticalPioneerEntity = statisticalPioneerDao.findByID(id);
		if (statisticalPioneerEntity == null) {
			throw new BusinessException("未找到相应数据");
		}
		ResStatisticalPioneer resStatisticalPioneer = new ResStatisticalPioneer(statisticalPioneerEntity);
		return resStatisticalPioneer;
	}
	
	public String createId(long playerId,int date){
		 return new StringBuffer().append(date).append("_").append(playerId).toString();
	  }
	
	/**
	 * 根据时间查询护眼先锋总数
	 */
	public long getByTimePioneerCount(int date) {
		return statisticalPioneerDao.getByTimePioneerCount(date);
	}
}
