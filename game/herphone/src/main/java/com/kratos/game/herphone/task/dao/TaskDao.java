package com.kratos.game.herphone.task.dao;

import java.util.List;

import org.hutu.mongo.dao.BaseDao;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kratos.game.herphone.task.entity.TaskEntity;

@Repository
public class TaskDao extends BaseDao<TaskEntity>{

	private static final String PLAYERID = "playerId";
	private static final String TASKID = "taskId";
	
	/**
	 * 用户查询当天对应的任务信息
	 */
	public TaskEntity taskSchedule(long playerId,int taskId) {
		Criteria criteria = new Criteria(PLAYERID).is(playerId).and(TASKID).is(taskId);
		Query query = new Query(criteria);
		return super.findOne(query);
	}
	
	/**
	 * 查询指定用户所有任务信息
	 */
	public List<TaskEntity> taskScheduleAll(long playerId) {
		Criteria criteria = new Criteria(PLAYERID).is(playerId);
		Query query = new Query(criteria);
		return super.find(query);
	}

}
