package com.kratos.game.herphone.task.bean;

import lombok.Data;

@Data
public class StageTaskBean {
	
	int taskId;//任务id
    int value; //值 ，用于判定是否达到完成条件
    int rewardState;//领取状态
    int relative; //任务相对
    
    public StageTaskBean() {
    	
	}
    
	public StageTaskBean(int taskId, int value, int rewardState,int relative) {
		this.taskId = taskId;
		this.value = value;
		this.rewardState = rewardState;
		this.relative = relative;
	}
}
