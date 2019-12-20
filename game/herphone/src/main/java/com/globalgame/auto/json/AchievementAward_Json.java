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
public class AchievementAward_Json{
	/** 编号::*/
	private Integer	id;
	/** 阶段奖励::*/
	private List<IntTuple>	reward;
	/** 奖励描述::*/
	private String	rewardsText;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 阶段奖励::*/
	public List<IntTuple> getReward(){
		return this.reward;
	}
	/** 奖励描述::*/
	public String getRewardsText(){
		return this.rewardsText;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**阶段奖励::*/
	public void setReward(List<IntTuple> reward){
		this.reward = reward;
	}
	/**奖励描述::*/
	public void setRewardsText(String rewardsText){
		this.rewardsText = rewardsText;
	}
}