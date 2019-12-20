package com.kratos.game.herphone.task.bean;

import lombok.Data;

@Data
public class PlayerGetAwardBean {

	int taskId;
	int taskType;
	
	public PlayerGetAwardBean() {
	}

	public PlayerGetAwardBean(int taskId, int taskType) {
		this.taskId = taskId;
		this.taskType = taskType;
	}
	
	
}
