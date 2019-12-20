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
public class LevelReward_Json{
	/** 序号::*/
	private Integer	id;
	/** 等级::*/
	private Integer	level;
	/** 奖励列表::*/
	private List<IntTuple>	list;

	/** 序号::*/
	public Integer getId(){
		return this.id;
	}
	/** 等级::*/
	public Integer getLevel(){
		return this.level;
	}
	/** 奖励列表::*/
	public List<IntTuple> getList(){
		return this.list;
	}
	/**序号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**等级::*/
	public void setLevel(Integer level){
		this.level = level;
	}
	/**奖励列表::*/
	public void setList(List<IntTuple> list){
		this.list = list;
	}
}