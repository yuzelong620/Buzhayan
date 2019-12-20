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
public class TaskAward_Json{
	/** 任务编号::*/
	private Integer	id;
	/** 奖励值::*/
	private List<IntTuple>	power;
	/** 任务类型::*/
	private Integer	type;
	/** 任务总进度::*/
	private Integer	sunSchedule;

	/** 任务编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 奖励值::*/
	public List<IntTuple> getPower(){
		return this.power;
	}
	/** 任务类型::*/
	public Integer getType(){
		return this.type;
	}
	/** 任务总进度::*/
	public Integer getSunSchedule(){
		return this.sunSchedule;
	}
	/**任务编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**奖励值::*/
	public void setPower(List<IntTuple> power){
		this.power = power;
	}
	/**任务类型::*/
	public void setType(Integer type){
		this.type = type;
	}
	/**任务总进度::*/
	public void setSunSchedule(Integer sunSchedule){
		this.sunSchedule = sunSchedule;
	}
}