package com.kratos.game.herphone.task.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.globalgame.auto.json.StageTask_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.StageTaskCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.task.bean.StageTaskBean;
import com.kratos.game.herphone.task.bean.StageTaskEntity;
import com.mind.core.util.IntTuple;

@Component
public class StageTaskService extends BaseService {

	public static final int STARCOIN = 2001;

	public static final int XP = 2002;

	private StageTaskEntity load(long playerId, int taskId, int stage, int relative) {
		StageTaskEntity stageTaskEntity = stageTaskDao.findByID(playerId + "_" + taskId);
		if (stageTaskEntity == null) {
			stageTaskEntity = new StageTaskEntity(playerId + "_" + taskId, taskId, playerId, stage, 0, 0, relative);
			stageTaskDao.save(stageTaskEntity);
		}
		return stageTaskEntity;
	}

	/**
	 * 阶段任务列表
	 */
	public List<StageTaskBean> StageTaskList() {
		Player player = PlayerContext.getPlayer();
		StageTaskCache stageTaskCache = JsonCacheManager.getCache(StageTaskCache.class);
		List<StageTask_Json> stageTask_JsonList = stageTaskCache.getList();
		List<StageTaskEntity> stageTaskEntityList = stageTaskDao.findTaskDataAllByPlayerIdAndStateNone(player.getId());
		stageTaskEntityList.addAll(stageTaskDao.findTaskDataAllByPlayerIdAndStageThree(player.getId()));
		List<StageTaskBean> stageTaskBeanList = new ArrayList<StageTaskBean>();
		Set<Integer> set = new HashSet<Integer>();
		for (StageTask_Json stageTask_Json : stageTask_JsonList) {
			StageTaskEntity stageTaskEntity = null;
			for (StageTaskEntity entity : stageTaskEntityList) {
				if (stageTask_Json.getRelative().equals(entity.getRelative())) { // 未完成或者未领取或者第三阶段的任务信息
					if (entity.getGetState() == CommonCost.StageTaskGetState.none.ordinal()
								||entity.getStage() == CommonCost.TaskStage.stageThree.ordinal()) {
						stageTaskEntity = entity;
						break;
					}
				}
			}
			StageTaskBean stageTaskBean = new StageTaskBean();
			if (!set.contains(stageTask_Json.getRelative())) {
				if (stageTaskEntity == null) {
					stageTaskBean.setTaskId(stageTask_Json.getId());
					stageTaskBean.setValue(0);
					stageTaskBean.setRewardState(CommonCost.StageTaskGetState.none.ordinal());
					set.add(stageTask_Json.getRelative());
				} else {
					stageTaskBean.setTaskId(stageTaskEntity.getTaskId());
					stageTaskBean.setValue(stageTaskEntity.getValue());
					stageTaskBean.setRewardState(stageTaskEntity.getGetState());
					set.add(stageTaskEntity.getRelative());
				}
				stageTaskBeanList.add(stageTaskBean);
			}
		}
		return stageTaskBeanList;
	}

	/**
	 * 任务进度更新
	 */
	public void updateTaskValue(int relative, int value) {
		Player player = PlayerContext.getPlayer();
		StageTaskCache stageTaskCache = JsonCacheManager.getCache(StageTaskCache.class);
		List<StageTask_Json> stageTask_JsonList = stageTaskCache.getList();
		for (StageTask_Json json : stageTask_JsonList) {
			if (json.getRelative().equals(relative)) {
				StageTaskEntity stageTaskEntity = load(player.getId(), json.getId(), json.getStage(), relative);
//				if (stageTaskEntity.getGetState() == CommonCost.StageTaskGetState.haveGot.ordinal()) {// 领取过了
//					continue;
//				}
				stageTaskEntity.setValue(stageTaskEntity.getValue() + value); // 任务进度 + value
				stageTaskDao.save(stageTaskEntity);
			}
		}
	}

	/**
	 * 任务领取奖励
	 */
	public List<IntTuple> taskGetAward(int taskId) {
		Player player = PlayerContext.getPlayer();
		PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(player.getId());
		StageTaskCache stageTaskCache = JsonCacheManager.getCache(StageTaskCache.class);
		StageTask_Json stageTask_Json = stageTaskCache.getData(taskId);
		StageTaskEntity stageTaskEntity = load(player.getId(), stageTask_Json.getId(), stageTask_Json.getStage(),
				stageTask_Json.getRelative());
		if (stageTaskEntity.getGetState() == CommonCost.StageTaskGetState.haveGot.ordinal()) { // 领取过了
			throw new BusinessException("已经领取过了");
		}
//		if (stageTaskEntity.getGetState() == CommonCost.StageTaskGetState.none.ordinal()) {
//			throw new BusinessException("没有达到需求");
//		}
		List<IntTuple> intTupleList = stageTask_Json.getTaskAward();
		for (IntTuple intTuple : intTupleList) {
			if (intTuple.getKey() == XP) { // 经验奖励
				playerDynamicDao.addExp(playerDynamicEntity.getPlayerId(), intTuple.getValue());
			}
			if (intTuple.getKey() == STARCOIN) { // 增加星币
				int starCoin = intTuple.getValue();
				playerDynamicDao.updateCurrency(playerDynamicEntity.getPlayerId(), starCoin);
			}
		}
		stageTaskEntity.setGetState(CommonCost.StageTaskGetState.haveGot.ordinal()); //设置为已领取
		stageTaskDao.save(stageTaskEntity);
		return intTupleList;
	}

}
