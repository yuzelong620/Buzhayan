package com.kratos.game.herphone.task.bean;

import lombok.Data;

@Data
public class TaskBean {
	
    int taskId;//任务id
    int value; //值 ，用于判定是否达到完成条件
    int rewardState;//领取状态
    
    public TaskBean() {
    	
	}
    
	public TaskBean(int taskId, int value, int rewardState) {
		this.taskId = taskId;
		this.value = value;
		this.rewardState = rewardState;
	}
}
