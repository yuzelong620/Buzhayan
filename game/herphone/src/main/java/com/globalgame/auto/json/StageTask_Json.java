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
public class StageTask_Json{
	/** 任务编号::*/
	private Integer	id;
	/** 任务阶段::*/
	private Integer	stage;
	/** 任务奖励::*/
	private List<IntTuple>	taskAward;
	/** 任务总进度::*/
	private Integer	sunSchedule;
	/** 任务相对::*/
	private Integer	relative;

	/** 任务编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 任务阶段::*/
	public Integer getStage(){
		return this.stage;
	}
	/** 任务奖励::*/
	public List<IntTuple> getTaskAward(){
		return this.taskAward;
	}
	/** 任务总进度::*/
	public Integer getSunSchedule(){
		return this.sunSchedule;
	}
	/** 任务相对::*/
	public Integer getRelative(){
		return this.relative;
	}
	/**任务编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**任务阶段::*/
	public void setStage(Integer stage){
		this.stage = stage;
	}
	/**任务奖励::*/
	public void setTaskAward(List<IntTuple> taskAward){
		this.taskAward = taskAward;
	}
	/**任务总进度::*/
	public void setSunSchedule(Integer sunSchedule){
		this.sunSchedule = sunSchedule;
	}
	/**任务相对::*/
	public void setRelative(Integer relative){
		this.relative = relative;
	}
}