package com.kratos.game.herphone.task.entity;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class TaskEntity {
	@Id
	String id;
	long playerId;//玩家id
    int taskId;//任务id
    int value; //值 ，用于判定是否达到完成条件
    int rewardState;//领取状态
    long nowDate; //当前时间 
    
    public TaskEntity() {
    	
	}
    
	public TaskEntity(String id, long playerId, int taskId, int value, int rewardState, long nowDate) {
		this.id = id;
		this.playerId = playerId;
		this.taskId = taskId;
		this.value = value;
		this.rewardState = rewardState;
		this.nowDate = nowDate;
	}
}
