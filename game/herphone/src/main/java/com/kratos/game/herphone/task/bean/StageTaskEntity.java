package com.kratos.game.herphone.task.bean;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class StageTaskEntity {

	@Id
	private String id;    
	private int taskId; //任务id
	private long playerId;
	private int stage; //任务阶段
	private int value; //当前进度
	private int getState; //领取状态 0未领取 1可领取 2领取完成
	private int relative; //任务对应
	
	public StageTaskEntity() {
		
	}

	public StageTaskEntity(String id, int taskId, long playerId, int stage,
			int value,int getState,int relative) {
		this.id = id;
		this.taskId = taskId;
		this.playerId = playerId;
		this.stage = stage;
		this.value = value;
		this.getState = getState;
		this.relative = relative;
	}
	
}
