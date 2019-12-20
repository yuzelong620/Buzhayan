package com.kratos.game.herphone.task.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.task.bean.StageTaskEntity;



@Repository
public class StageTaskDao extends BaseDao<StageTaskEntity>{
	
	private static final String PLAYERID = "playerId";
	private static final String GETSTATE = "getState";
	private static final String STAGE = "stage";
	
	/**
	 * 查询指定用户所有未领取状态的任务
	 */
	public List<StageTaskEntity> findTaskDataAllByPlayerIdAndStateNone(long playerId){
		Criteria criteria = new Criteria(PLAYERID).is(playerId).and(GETSTATE)
				.is(CommonCost.StageTaskGetState.none.ordinal());
		Query query = new Query(criteria);
		return super.find(query);
	}
	
	/**
	 * 查询指定用户所有第三阶段的任务
	 */
	public List<StageTaskEntity> findTaskDataAllByPlayerIdAndStageThree(long playerId){
		Criteria criteria = new Criteria(PLAYERID).is(playerId).and(STAGE)
				.is(CommonCost.TaskStage.stageThree.ordinal());
		Query query = new Query(criteria);
		return super.find(query);
	}
	
}
