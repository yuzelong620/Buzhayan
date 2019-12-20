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
public class Gift_Json{
	/** 编号::*/
	private Integer	id;
	/** 名字::*/
	private String	name;
	/** 值（增加好感度）::*/
	private Integer	value;
	/** 价值::*/
	private Integer	cost;

	/** 编号::*/
	public Integer getId(){
		return this.id;
	}
	/** 名字::*/
	public String getName(){
		return this.name;
	}
	/** 值（增加好感度）::*/
	public Integer getValue(){
		return this.value;
	}
	/** 价值::*/
	public Integer getCost(){
		return this.cost;
	}
	/**编号::*/
	public void setId(Integer id){
		this.id = id;
	}
	/**名字::*/
	public void setName(String name){
		this.name = name;
	}
	/**值（增加好感度）::*/
	public void setValue(Integer value){
		this.value = value;
	}
	/**价值::*/
	public void setCost(Integer cost){
		this.cost = cost;
	}
}