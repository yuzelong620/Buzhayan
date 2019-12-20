package com.globalgame.auto.json;
import java.util.List;
import com.mind.core.util.StringIntTuple;
import com.mind.core.util.IntDoubleTuple;
import com.mind.core.util.IntTuple;
import com.mind.core.util.ThreeTuple;
import com.mind.core.util.StringFloatTuple;

/**
*自动生成类
*/
public class TaskInformation_Json{
	/** 任务编号::*/
	private Integer	id;
	/** 描述::*/
	private String	describe;
	/** 任务总进度::*/
	private Integer	sunSchedule;
	/** 任务类型::*/
	private Integer	taskType;
	/** 进度::*/
	private Integer	value;
	/** 状态::*/
	private Integer	rewardState;
	/** 是否完成::*/
	private Integer	progress;

	/** 任务编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 描述::*/
	public String getDescribe(){
		return this.describe;
	}
	/** 任务总进度::*/
	public Integer getSunSchedule(){
		return this.sunSchedule;
	}
	/** 任务类型::*/
	public Integer getTaskType(){
		return this.taskType;
	}
	/** 进度::*/
	public Integer getValue(){
		return this.value;
	}
	/** 状态::*/
	public Integer getRewardState(){
		return this.rewardState;
	}
	/** 是否完成::*/
	public Integer getProgress(){
		return this.progress;
	}
	/**任务编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**描述::*/
	public void setDescribe(String describe){
		this.describe = describe;
	}
	/**任务总进度::*/
	public void setSunSchedule(Integer sunSchedule){
		this.sunSchedule = sunSchedule;
	}
	/**任务类型::*/
	public void setTaskType(Integer taskType){
		this.taskType = taskType;
	}
	/**进度::*/
	public void setValue(Integer value){
		this.value = value;
	}
	/**状态::*/
	public void setRewardState(Integer rewardState){
		this.rewardState = rewardState;
	}
	/**是否完成::*/
	public void setProgress(Integer progress){
		this.progress = progress;
	}
}