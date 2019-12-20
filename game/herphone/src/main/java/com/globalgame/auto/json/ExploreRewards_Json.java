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
public class ExploreRewards_Json{
	/** 编号::*/
	private Integer	id;
	/** 阶段分数::*/
	private Integer	StageFraction;
	/** 赠送奖励::*/
	private String	Rewards;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 阶段分数::*/
	public Integer getStageFraction(){
		return this.StageFraction;
	}
	/** 赠送奖励::*/
	public String getRewards(){
		return this.Rewards;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**阶段分数::*/
	public void setStageFraction(Integer StageFraction){
		this.StageFraction = StageFraction;
	}
	/**赠送奖励::*/
	public void setRewards(String Rewards){
		this.Rewards = Rewards;
	}
}