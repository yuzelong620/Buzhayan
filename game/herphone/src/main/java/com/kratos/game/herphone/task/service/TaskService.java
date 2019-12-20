package com.kratos.game.herphone.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.globalgame.auto.json.TaskAward_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.TaskAwardCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.task.bean.PlayerGetAwardBean;
import com.kratos.game.herphone.task.bean.TaskBean;
import com.kratos.game.herphone.task.entity.TaskEntity;
import com.kratos.game.herphone.util.DateUtil;
import com.mind.core.util.IntTuple;

@Component
public class TaskService extends BaseService {
	
	private static final int STARCOIN = 2001; //货币
	
	private static final int XP = 2002; //经验
	

	public TaskEntity load(long playerId,int taskId) {
		TaskEntity taskEntity = taskDao.taskSchedule(playerId,taskId);
		if(taskEntity == null) {
			String id = UUID.randomUUID().toString().replace("-", "");
			taskEntity = new TaskEntity(id, playerId, taskId, 0, 0, System.currentTimeMillis());
			taskDao.save(taskEntity);
			return taskEntity;
		}
		return taskEntity;
	}
	
	/**
	 * 用户加载每日任务进度
	 */
	public List<TaskBean> taskScheduleAll() {
		Player player = PlayerContext.getPlayer();
		List<TaskBean> taskBeanList = new ArrayList<TaskBean>();
		TaskAwardCache cache = JsonCacheManager.getCache(TaskAwardCache.class);
		List<TaskAward_Json> taskAward_Json = cache.getList();
		List<TaskEntity> taskEntityAll = taskDao.taskScheduleAll(player.getId());
		for (TaskAward_Json award_Json : taskAward_Json) {
			TaskEntity taskEntity = null;
			for (TaskEntity entity : taskEntityAll) {
				if(award_Json.getId().equals(entity.getTaskId())) {
					taskEntity = entity;
				}
			}
			if(taskEntity == null) {
				taskBeanList.add(new TaskBean(award_Json.getId(),0,0));
			} else {
				if(examineDate(taskEntity)) { //检测是否是同一天
					taskBeanList.add(new TaskBean(taskEntity.getTaskId(),0,0));
				} else {
					taskBeanList.add(new TaskBean(taskEntity.getTaskId(), taskEntity.getValue(), taskEntity.getRewardState()));
				}
			}
		}
		return taskBeanList;
	}

	/**
	 * 用户任务进度更新
	 */
	@Deprecated
	public void playerScheduleUpdate(int taskType) {
		//Player player = PlayerContext.getPlayer();
		//List<TaskEntity> taskEntityList = load(player.getId());
		//TaskAwardCache cache = JsonCacheManager.getCache(TaskAwardCache.class);
		//List<TaskAward_Json> taskAward_Json = cache.getList();
		//for (int i = 0; i < taskAward_Json.size(); i++) {
		//	if (taskAward_Json.get(i).getType().equals(taskType)) {
		//		taskEntityList.get(i).setValue(taskEntityList.get(i).getValue() + 1);
		//		taskDao.save(taskEntityList.get(i));
		//	}
		//}
		playerScheduleUpdate(taskType,1);
	}
	
	/**
	 * 用户任务进度更新
	 */
	public void playerScheduleUpdate(int taskType,int value) {
		Player player = PlayerContext.getPlayer();
		TaskEntity taskEntity = null;
		TaskAwardCache cache = JsonCacheManager.getCache(TaskAwardCache.class);
		List<TaskAward_Json> taskAward_Json = cache.getList();
		for (TaskAward_Json award_Json : taskAward_Json) {
			if(award_Json.getType().equals(taskType)) {
				taskEntity = load(player.getId(), award_Json.getId());
				if(examineDate(taskEntity)) { //检测日期
					taskEntity = taskDao.taskSchedule(player.getId(), award_Json.getId());
					taskEntity.setValue(taskEntity.getValue() + value);
					taskDao.save(taskEntity);
				} else {
					taskEntity.setValue(taskEntity.getValue() + value);
					taskDao.save(taskEntity);
				}
			}
		}
	}
	
	/**
	 * 用户领取奖励
	 */
	public List<IntTuple> playerGetAward(PlayerGetAwardBean playerGetAwardBean) {
		Player player = PlayerContext.getPlayer();
		TaskEntity taskEntity = load(player.getId(),playerGetAwardBean.getTaskId());
		TaskAwardCache cache = JsonCacheManager.getCache(TaskAwardCache.class);
		List<TaskAward_Json> taskAward_JsonList = cache.getTaskAward_Json(playerGetAwardBean.getTaskType());
			for (TaskAward_Json award_Json : taskAward_JsonList) {
				if(playerGetAwardBean.getTaskType() == CommonCost.TackType.watchVideo.ordinal()|| 
						 playerGetAwardBean.getTaskType() == CommonCost.TackType.dailyShare.ordinal()) {//判断是否是视频任务或分享任务
					taskEntity.setValue(taskEntity.getValue()+1); //任务进度+1
					taskDao.save(taskEntity);
					return awardDispose(taskEntity,award_Json,player); //领取奖励处理
				}
				if(award_Json.getId().equals(playerGetAwardBean.getTaskId()) && 
						playerGetAwardBean.getTaskId() == taskEntity.getTaskId()) { //判断任务ID是否相等
					return awardDispose(taskEntity,award_Json,player); 
				}
		}
		return null;
	}
	
	//领取奖励处理
	private List<IntTuple> awardDispose(TaskEntity taskEntity,TaskAward_Json award_Json,Player player){
		if(taskEntity.getRewardState() == 0) { //判断是否是未领取状态
			if(taskEntity.getValue() < award_Json.getSunSchedule()) {
				throw new BusinessException("没有达成目标");
			}
			PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(player.getId());
			List<IntTuple> intTupleList = award_Json.getPower();
			for (IntTuple intTuple : intTupleList) {
				if(intTuple.getKey() == XP) { //经验奖励
					int xp =intTuple.getValue();
					playerDynamicDao.addExp(playerDynamicEntity.getPlayerId(), xp);
				}
				if(intTuple.getKey() == STARCOIN) { //增加星币
					int starCoin = intTuple.getValue();
					playerDynamicDao.updateCurrency(playerDynamicEntity.getPlayerId(), starCoin);
				}
			}
		    playerServiceImpl.cacheAndPersist(player.getId(), player);
		    taskEntity.setRewardState(1); //设为领取状态
			taskDao.save(taskEntity);
		    return award_Json.getPower();
		} else {
			throw new BusinessException("不能重复领取");
		}
	}
	
	//检查日期
	private boolean examineDate(TaskEntity taskEntity) {
		long nowDate = System.currentTimeMillis();
		long time = taskEntity.getNowDate();
		//不是同一天
		if(!DateUtil.isSameDate(nowDate, time)) {
			taskEntity.setValue(0);
			taskEntity.setRewardState(0);
			taskEntity.setNowDate(nowDate);
			taskDao.save(taskEntity);
			return true;
		}
		return false;
	}

}
